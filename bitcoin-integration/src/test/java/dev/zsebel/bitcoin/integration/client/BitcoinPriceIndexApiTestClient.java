package dev.zsebel.bitcoin.integration.client;

import dev.zsebel.bitcoin.integration.model.Currency;
import dev.zsebel.bitcoin.integration.model.Header;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class BitcoinPriceIndexApiTestClient {

    private final WebTestClient webTestClient;

    public BitcoinPriceIndexApiTestClient(final WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public WebTestClient.ResponseSpec getBitcoinPriceIndexIn(final Currency currency) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/v2/bitcoin")
                .queryParam("currency", currency.name())
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .header(Header.DEBUG_TRACE, "enabled")
            .exchange();
    }
}
