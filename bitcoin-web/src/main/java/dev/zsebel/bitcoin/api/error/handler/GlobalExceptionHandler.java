package dev.zsebel.bitcoin.api.error.handler;

import java.util.List;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.zsebel.bitcoin.api.error.ApiError;
import dev.zsebel.bitcoin.api.error.InternalError;
import dev.zsebel.bitcoin.api.error.ValidationError;
import dev.zsebel.bitcoin.api.model.ApiErrorResponse;
import dev.zsebel.bitcoin.client.exception.CoinbaseClientException;
import dev.zsebel.bitcoin.client.exception.CoinbaseClientInternalServerError;
import dev.zsebel.bitcoin.client.exception.CoinbaseClientRetryExhaustedException;
import dev.zsebel.bitcoin.client.exception.CoinbaseClientTimeoutException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(final ConstraintViolationException violationException, final HttpServletRequest request) {
        List<ApiError> validationErrors = collectApiValidationErrors(violationException);
        return buildApiErrorResponse(request.getRequestURI(), HttpStatus.BAD_REQUEST, validationErrors);
    }

    @ExceptionHandler(CoinbaseClientInternalServerError.class)
    public ResponseEntity<ApiErrorResponse> handleCoinbaseClientInternalServerError(
        final CoinbaseClientInternalServerError coinbaseClientInternalServerError, final HttpServletRequest request
    ) {
        LOGGER.error(coinbaseClientInternalServerError.getMessage());
        InternalError error = new InternalError("The request failed due to an internal downstream error.");
        return buildApiErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, List.of(error));
    }

    @ExceptionHandler(CoinbaseClientException.class)
    public ResponseEntity<ApiErrorResponse> handleCoinbaseClientException(
        final CoinbaseClientException coinbaseClientException, final HttpServletRequest request
    ) {
        LOGGER.error(coinbaseClientException.getMessage());
        InternalError error = new InternalError("The request failed due to a Coinbase client error.");
        return buildApiErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, List.of(error));
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<ApiErrorResponse> handleCallNotPermittedException(final CallNotPermittedException callNotPermittedException, final HttpServletRequest request) {
        LOGGER.error(callNotPermittedException.getMessage());
        InternalError error = new InternalError("The service is temporarily unavailable due to an issue with a downstream dependency. Please try again later.");
        return buildApiErrorResponse(request.getRequestURI(), HttpStatus.SERVICE_UNAVAILABLE, List.of(error));
    }

    @ExceptionHandler(CoinbaseClientTimeoutException.class)
    public ResponseEntity<ApiErrorResponse> handleCoinbaseClientTimeoutException(
        final CoinbaseClientTimeoutException coinbaseClientTimeoutException, final HttpServletRequest request
    ) {
        LOGGER.error(coinbaseClientTimeoutException.getMessage());
        InternalError error = new InternalError("The request timed out due to a downstream operation took too long to complete.");
        return buildApiErrorResponse(request.getRequestURI(), HttpStatus.REQUEST_TIMEOUT, List.of(error));
    }

    @ExceptionHandler(CoinbaseClientRetryExhaustedException.class)
    public ResponseEntity<ApiErrorResponse> handleCoinbaseClientRetryExhaustedException(
        final CoinbaseClientRetryExhaustedException coinbaseClientRetryExhaustedException,
        final HttpServletRequest request
    ) {
        LOGGER.error("Retries are exhausted: {} out of 2 retries failed.", coinbaseClientRetryExhaustedException.getTotalRetries());
        InternalError error = new InternalError("The request failed after exhausting all retry attempts due to persistent downstream errors.");
        return buildApiErrorResponse(request.getRequestURI(), HttpStatus.SERVICE_UNAVAILABLE, List.of(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedExceptions(final Exception exception, final HttpServletRequest request) {
        LOGGER.error("An unexpected error occurred. Details: {}", exception.getMessage());
        List<ApiError> errors = List.of(new InternalError("An unexpected error occurred."));
        return buildApiErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, errors);
    }

    private List<ApiError> collectApiValidationErrors(final ConstraintViolationException violationException) {
        return violationException.getConstraintViolations()
            .stream()
            .map(this::createApiValidationError)
            .toList();
    }

    private ApiError createApiValidationError(final ConstraintViolation<?> violation) {
        String requestParameter = getRequestParameter(violation.getPropertyPath());
        return new ValidationError(requestParameter, violation.getMessage());
    }

    private String getRequestParameter(final Path propertyPath) {
        Path.Node lastNode = null;
        for (Path.Node node : propertyPath) {
            lastNode = node;
        }
        return lastNode != null ? lastNode.toString() : "";
    }

    private ResponseEntity<ApiErrorResponse> buildApiErrorResponse(final String uri, final HttpStatus httpStatus, final List<ApiError> errors) {
        return ResponseEntity
            .status(httpStatus)
            .body(new ApiErrorResponse(httpStatus.value(), uri, errors));
    }
}