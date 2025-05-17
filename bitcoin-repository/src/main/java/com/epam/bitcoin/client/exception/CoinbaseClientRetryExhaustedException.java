package com.epam.bitcoin.client.exception;

public class CoinbaseClientRetryExhaustedException extends RuntimeException {
    
    private final long totalRetries;

    public CoinbaseClientRetryExhaustedException(final long totalRetries) {
        super("Coinbase client timed out.");
        this.totalRetries = totalRetries;
    }

    public long getTotalRetries() {
        return totalRetries;
    }
}
