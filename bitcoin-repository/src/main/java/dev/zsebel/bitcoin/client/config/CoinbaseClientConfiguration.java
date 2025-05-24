package dev.zsebel.bitcoin.client.config;

import java.time.Duration;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import dev.zsebel.bitcoin.client.CoinbaseExchangeRatesClient;
import dev.zsebel.bitcoin.client.config.RetryConfiguration.RetryProperties;
import dev.zsebel.bitcoin.client.factory.ReactiveContextFactory;
import dev.zsebel.bitcoin.client.filters.CoinbaseClientErrorHandlingFilter;
import dev.zsebel.bitcoin.logging.filters.WebClientLoggingFilter;


@Configuration
public class CoinbaseClientConfiguration {

    private static final String CLIENT_ID_HEADER = "Client-Id";
    private static final String COINBASE_INSTANCE_IDENTIFIER = "coinbase";

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${coinbase.timeout}")
    private int timeout;

    @Value("${coinbase.baseUrl}")
    private String baseUrl;

    @Value("${coinbase.endpoint}")
    private String endpoint;

    @Value("${coinbase.bitcoinSymbol}")
    private String bitcoinSymbol;

    @Bean
    public HttpClient coinbaseHttpClient() {
        return HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
            .responseTimeout(Duration.ofMillis(timeout));
    }

    @Bean
    public ReactorClientHttpConnector coinbaseHttpClientConnector(final HttpClient coinbaseHttpClient) {
        return new ReactorClientHttpConnector(coinbaseHttpClient);
    }

    @Bean
    public WebClient coinbaseWebClient(
        final WebClient.Builder webClientBuilder,
        final ReactorClientHttpConnector coinbaseHttpClientConnector,
        final WebClientLoggingFilter webClientLoggingFilter,
        final CoinbaseClientErrorHandlingFilter coinbaseClientErrorHandlingFilter
    ) {
        return webClientBuilder
            .baseUrl(baseUrl)
            .clientConnector(coinbaseHttpClientConnector)
            .defaultHeader(CLIENT_ID_HEADER, applicationName)
            .filter(webClientLoggingFilter.logRequest())
            .filter(webClientLoggingFilter.logResponse())
            .filter(coinbaseClientErrorHandlingFilter)
            .build();
    }

    @Bean
    public ReactiveCircuitBreaker coinbaseReactiveCircuitBreaker(final ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        return reactiveCircuitBreakerFactory.create(COINBASE_INSTANCE_IDENTIFIER);
    }

    @Bean
    public RetryBackoffSpec coinbaseRetrySpec(final RetryConfiguration retryConfiguration) {
        RetryProperties coinbaseRetryProperties = retryConfiguration.instances().get(COINBASE_INSTANCE_IDENTIFIER);
        return Retry.backoff(coinbaseRetryProperties.maxAttempts(), coinbaseRetryProperties.waitDuration());
    }

    @Bean
    public CoinbaseExchangeRatesClient coinbaseExchangeRatesClient(final WebClient coinbaseWebClient, final ReactiveContextFactory reactiveContextFactory) {
        return new CoinbaseExchangeRatesClient(coinbaseWebClient, endpoint, bitcoinSymbol, reactiveContextFactory);
    }
}
