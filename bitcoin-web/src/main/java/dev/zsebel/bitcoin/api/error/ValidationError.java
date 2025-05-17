package dev.zsebel.bitcoin.api.error;

public record ValidationError(String requestParameter, String message) implements ApiError {

    @Override
    public ErrorType type() {
        return ErrorType.VALIDATION_ERROR;
    }
}
