package dev.zsebel.bitcoin.integration.stub;

import dev.zsebel.bitcoin.integration.BaseIntegrationTest;
import dev.zsebel.bitcoin.integration.stub.config.CoinbaseClientStubConfiguration;
import dev.zsebel.bitcoin.integration.stub.client.CoinbaseClientStub;
import dev.zsebel.bitcoin.integration.support.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * but CoinbaseClient is replaced by {@link CoinbaseClientStub}
 */
@ActiveProfiles("stub")
@Import(CoinbaseClientStubConfiguration.class)
class StubbedClientIntegrationTest extends BaseIntegrationTest {

    @Test
    void testBitcoinPriceIndexShouldReturnTheFormattedBitcoinPriceWhenCoinbaseClientIsMockedViaCoinbaseClientMock() {
        // GIVEN
        var expectedBitcoinPrice = "$95,472.37";

        // WHEN + THEN
        bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(Currency.USD)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath(BITCOIN_PRICE_INDEX_JSON_PATH).isEqualTo(expectedBitcoinPrice);
    }
}
