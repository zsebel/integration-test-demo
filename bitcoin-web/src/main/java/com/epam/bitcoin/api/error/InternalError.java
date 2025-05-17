package com.epam.bitcoin.api.error;

public record InternalError(String message) implements ApiError {

    @Override
    public ErrorType type() {
        return ErrorType.INTERNAL_ERROR;
    }
}
