package com.epam.bitcoin.client.exception;

public class CoinbaseClientTimeoutException extends RuntimeException {

    public CoinbaseClientTimeoutException(String message) {
        super(message);
    }
}
