package dev.zsebel.bitcoin.client;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import dev.zsebel.bitcoin.client.exception.CoinbaseClientRetryExhaustedException;
import dev.zsebel.bitcoin.client.exception.CoinbaseClientTimeoutException;
import dev.zsebel.bitcoin.client.model.CoinbaseResponse;

@Component
public class CoinbaseExchangeRatesClientWrapper implements ExchangeRatesClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinbaseExchangeRatesClientWrapper.class);
    private static final String RETRY_ATTEMPT_MESSAGE_TEMPLATE = "Retry attempt #{}";

    private final CoinbaseExchangeRatesClient coinbaseExchangeRatesClient;
    private final ReactiveCircuitBreaker reactiveCircuitBreaker;
    private final RetryBackoffSpec retrySpec;

    @Autowired
    public CoinbaseExchangeRatesClientWrapper(
        final CoinbaseExchangeRatesClient coinbaseExchangeRatesClient,
        final ReactiveCircuitBreaker reactiveCircuitBreaker,
        final RetryBackoffSpec retrySpec
    ) {
        this.coinbaseExchangeRatesClient = coinbaseExchangeRatesClient;
        this.reactiveCircuitBreaker = reactiveCircuitBreaker;
        this.retrySpec = retrySpec;
    }

    @Override
    public Mono<CoinbaseResponse> fetchExchangeRates() {
        return reactiveCircuitBreaker.run(
            coinbaseExchangeRatesClient.fetchExchangeRates()
                .retryWhen(
                    retrySpec
                        .doBeforeRetry(retrySignal -> LOGGER.warn(RETRY_ATTEMPT_MESSAGE_TEMPLATE, retrySignal.totalRetries() + 1))
                        .onRetryExhaustedThrow(this::throwRetryExhaustedException)
                ),
            this::handleFallback
        );
    }

    private Throwable throwRetryExhaustedException(RetryBackoffSpec retryBackoffSpec, Retry.RetrySignal retrySignal) {
        throw new CoinbaseClientRetryExhaustedException(retrySignal.totalRetries());
    }

    private Mono<CoinbaseResponse> handleFallback(Throwable throwable) {
        return Mono.error(
            throwable instanceof TimeoutException
                ? new CoinbaseClientTimeoutException(throwable.getMessage())
                : throwable
        );
    }
}
