package com.epam.bitcoin.client.exception;

public class CoinbaseClientException extends RuntimeException{

    public CoinbaseClientException(final String errorMessage) {
        super(errorMessage);
    }
}
