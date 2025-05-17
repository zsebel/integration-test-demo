package dev.zsebel.bitcoin.client;

import dev.zsebel.bitcoin.client.factory.ReactiveContextFactory;
import dev.zsebel.bitcoin.client.model.CoinbaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * A WebClient implementation to fetch exchange rates from Coinbase for Bitcoin
 * via <a href="http://api.coinbase.com/v2/exchange-rates?currency=BTC">Coinbase Exchange Rates API</a>
 */
public class CoinbaseExchangeRatesClient implements ExchangeRatesClient {

    private static final String CURRENCY_QUERY_PARAM = "currency";

    private final WebClient coinbaseWebClient;
    private final String coinbaseEndpoint;
    private final String bitcoinSymbol;
    private final ReactiveContextFactory reactiveContextFactory;

    public CoinbaseExchangeRatesClient(
        final WebClient coinbaseWebClient,
        final String coinbaseEndpoint,
        final String bitcoinSymbol,
        final ReactiveContextFactory reactiveContextFactory
    ) {
        this.coinbaseWebClient = coinbaseWebClient;
        this.coinbaseEndpoint = coinbaseEndpoint;
        this.bitcoinSymbol = bitcoinSymbol;
        this.reactiveContextFactory = reactiveContextFactory;
    }

    @Override
    public Mono<CoinbaseResponse> fetchExchangeRates() {
        return coinbaseWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(coinbaseEndpoint)
                .queryParam(CURRENCY_QUERY_PARAM, bitcoinSymbol)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(CoinbaseResponse.class)
            .contextWrite(reactiveContextFactory.createContext());
    }
}
