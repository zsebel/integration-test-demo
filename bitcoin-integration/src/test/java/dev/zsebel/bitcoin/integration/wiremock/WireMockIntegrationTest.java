package dev.zsebel.bitcoin.integration.wiremock;

import dev.zsebel.bitcoin.integration.model.Currency;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * but CoinbaseClient requests are sent to a {@link WireMockServer} running on localhost
 * instead of the actual Coinbase endpoint.
 */
@ActiveProfiles("integration")
public class WireMockIntegrationTest extends BaseWireMockIntegrationTest {

    @Test
    public void testBitcoinPricesShouldReturnTheFormattedBitcoinPriceIndexWhenCoinbaseRequestsAreSentToWireMockServer() {
        // GIVEN
        var expectedBitcoinPrice = "$95,472.37";

        setupCoinbaseExchangeRatesSuccessStubWith(COINBASE_RESPONSE_MOCK_JSON_FILE);

        // WHEN + THEN
        bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(Currency.USD)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath(BITCOIN_PRICE_INDEX_JSON_PATH).isEqualTo(expectedBitcoinPrice);
    }
}
