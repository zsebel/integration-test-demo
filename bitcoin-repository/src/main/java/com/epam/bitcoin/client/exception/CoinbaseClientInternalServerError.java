package com.epam.bitcoin.client.exception;

public class CoinbaseClientInternalServerError extends RuntimeException {

    public CoinbaseClientInternalServerError(String message) {
        super(message);
    }
}
