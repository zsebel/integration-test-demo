package dev.zsebel.bitcoin.logging.interceptor;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggingServerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingServerInterceptor.class);
    private static final String CURRENCY_REQUEST_PARAM = "currency";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (DispatcherType.REQUEST == request.getDispatcherType() && isBitcoinApiRequest(request.getRequestURI())) {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String requestedCurrency = request.getParameter(CURRENCY_REQUEST_PARAM);
            LOGGER.info("Incoming {} request to [{}] endpoint with {} requested currency", method, uri, requestedCurrency);
        }
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        String uri = request.getRequestURI();
        if (isBitcoinApiRequest(uri)) {
            LOGGER.info("Response sent from [{}] endpoint with status: [{}]", uri, HttpStatus.resolve(response.getStatus()));
        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private static boolean isBitcoinApiRequest(String uri) {
        return uri.startsWith("/api/");
    }
}
