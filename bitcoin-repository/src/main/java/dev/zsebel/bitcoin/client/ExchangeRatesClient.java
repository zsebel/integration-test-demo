package dev.zsebel.bitcoin.client;

import dev.zsebel.bitcoin.client.model.CoinbaseResponse;
import reactor.core.publisher.Mono;

public interface ExchangeRatesClient {

    Mono<CoinbaseResponse> fetchExchangeRates();
}
