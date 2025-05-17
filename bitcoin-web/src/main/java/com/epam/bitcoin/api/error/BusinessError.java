package com.epam.bitcoin.api.error;

public record BusinessError(String message) implements ApiError {

    @Override
    public ErrorType type() {
        return ErrorType.BUSINESS_ERROR;
    }
}
