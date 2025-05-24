package dev.zsebel.bitcoin.client;

import reactor.core.publisher.Mono;

import dev.zsebel.bitcoin.client.model.CoinbaseResponse;

public interface ExchangeRatesClient {

    Mono<CoinbaseResponse> fetchExchangeRates();
}
