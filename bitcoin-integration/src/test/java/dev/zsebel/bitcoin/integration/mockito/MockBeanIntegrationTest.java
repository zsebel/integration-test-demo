package dev.zsebel.bitcoin.integration.mockito;

import dev.zsebel.bitcoin.client.ExchangeRatesClient;
import dev.zsebel.bitcoin.client.model.CoinbaseResponse;
import dev.zsebel.bitcoin.integration.BaseIntegrationTest;
import dev.zsebel.bitcoin.integration.stub.client.CoinbaseClientStub;
import dev.zsebel.bitcoin.integration.support.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * but CoinbaseClient bean is overridden in the ApplicationContext by {@link MockitoBean} (previously @MockBean)
 * to avoid the need of creating stubs manually such as {@link CoinbaseClientStub}.
 */
@ActiveProfiles("integration")
class MockBeanIntegrationTest extends BaseIntegrationTest {

    private static final String BITCOIN_CRYPTO_SYMBOL = "BTC";

    @MockitoBean
    private ExchangeRatesClient coinbaseExchangeRatesClientWrapper;

    @Test
    void testBitcoinPriceIndexShouldReturnTheFormattedBitcoinPriceWhenCoinbaseClientMockitoBeanIsUsed() {
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
