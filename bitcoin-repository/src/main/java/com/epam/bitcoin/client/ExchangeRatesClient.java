package com.epam.bitcoin.client;

import com.epam.bitcoin.client.model.CoinbaseResponse;
import reactor.core.publisher.Mono;

public interface ExchangeRatesClient {

    Mono<CoinbaseResponse> fetchExchangeRates();
}
