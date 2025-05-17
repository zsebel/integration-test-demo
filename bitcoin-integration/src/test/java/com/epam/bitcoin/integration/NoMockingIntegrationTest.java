package com.epam.bitcoin.integration;

import com.epam.bitcoin.integration.base.BaseIntegrationTest;
import com.epam.bitcoin.integration.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Basic integration test using {@link WebTestClient} without mocking the downstream Coinbase client.
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * <p>
 * This approach starts:
 * - Root WebApplicationContext
 * - Embedded Tomcat server
 * - DispatcherServlet
 */
public class NoMockingIntegrationTest extends BaseIntegrationTest {

    @Test
    public void testBitcoinPriceIndexShouldReturnTheCurrentFormattedBitcoinPriceWhenCoinbaseClientIsNotMocked() {
        bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(Currency.USD)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath(BITCOIN_PRICE_INDEX_JSON_PATH).isNotEmpty();
    }
}
