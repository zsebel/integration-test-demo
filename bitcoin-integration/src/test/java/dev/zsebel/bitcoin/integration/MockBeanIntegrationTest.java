package dev.zsebel.bitcoin.integration;

import dev.zsebel.bitcoin.client.ExchangeRatesClient;
import dev.zsebel.bitcoin.client.model.CoinbaseResponse;
import dev.zsebel.bitcoin.integration.base.BaseIntegrationTest;
import dev.zsebel.bitcoin.integration.client.mock.CoinbaseClientMock;
import dev.zsebel.bitcoin.integration.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * but CoinbaseClient is mocked using {@link MockitoBean}
 * to avoid the need of creating manual mocks such as {@link CoinbaseClientMock}.
 */
@ActiveProfiles("integration")
public class MockBeanIntegrationTest extends BaseIntegrationTest {

    private static final String BITCOIN_CRYPTO_SYMBOL = "BTC";

    @MockitoBean
    private ExchangeRatesClient coinbaseExchangeRatesClientWrapper;

    @Test
    public void testBitcoinPriceIndexShouldReturnTheFormattedBitcoinPriceWhenCoinbaseClientMockitoBeanIsUsed() {
        // GIVEN
        var bitcoinRawPrice = "12345.67";
        var bitcoinFormattedPrice = "$12,345.67";
        var bitcoinPriceRates = createBitcoinRates(bitcoinRawPrice);
        var coinbaseResponse = new CoinbaseResponse(bitcoinPriceRates);

        when(coinbaseExchangeRatesClientWrapper.fetchExchangeRates()).thenReturn(Mono.just(coinbaseResponse));

        // WHEN
        bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(Currency.USD)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath(BITCOIN_PRICE_INDEX_JSON_PATH).isEqualTo(bitcoinFormattedPrice);

        // THEN
        verify(coinbaseExchangeRatesClientWrapper).fetchExchangeRates();
    }

    private CoinbaseResponse.Data createBitcoinRates(final String bitcoinRawPrice) {
        return new CoinbaseResponse.Data(
            BITCOIN_CRYPTO_SYMBOL,
            Map.of(Currency.USD.name(), bitcoinRawPrice)
        );
    }
}
