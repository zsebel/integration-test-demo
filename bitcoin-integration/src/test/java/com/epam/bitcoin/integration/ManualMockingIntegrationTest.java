package com.epam.bitcoin.integration;

import com.epam.bitcoin.integration.base.BaseIntegrationTest;
import com.epam.bitcoin.integration.client.mock.CoinbaseClientMock;
import com.epam.bitcoin.integration.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * but CoinbaseClient is mocked via {@link CoinbaseClientMock}
 */
@ActiveProfiles("manual")
public class ManualMockingIntegrationTest extends BaseIntegrationTest {

    @Test
    public void testBitcoinPriceIndexShouldReturnTheFormattedBitcoinPriceWhenCoinbaseClientIsMockedViaCoinbaseClientMock() {
        // GIVEN
        var expectedBitcoinPrice = "$95,472.37";

        // WHEN + THEN
        bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(Currency.USD)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath(BITCOIN_PRICE_INDEX_JSON_PATH).isEqualTo(expectedBitcoinPrice);
    }
}
