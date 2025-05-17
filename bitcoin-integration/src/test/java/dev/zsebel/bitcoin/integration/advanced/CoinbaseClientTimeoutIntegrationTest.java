package dev.zsebel.bitcoin.integration.advanced;

import dev.zsebel.bitcoin.integration.model.Currency;
import dev.zsebel.bitcoin.integration.model.Header;
import dev.zsebel.bitcoin.integration.wiremock.BaseWireMockIntegrationTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import karate.com.linecorp.armeria.common.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * and CoinbaseClient requests are sent to a {@link WireMockServer} running on localhost
 * but in this scenario Coinbase client times out.
 */
@ActiveProfiles("integration")
public class CoinbaseClientTimeoutIntegrationTest extends BaseWireMockIntegrationTest {

    @Value("${resilience4j.timelimiter.configs.default.timeoutDuration}")
    private Duration timeoutInMillis;

    @Test
    public void testCoinbaseClientTimeoutShouldReturnErrorResponseWhenCoinbaseDownstreamCallTakesLongerThanTheTimeoutSetting() {
        // GIVEN
        stubFor(WireMock.get(urlEqualTo(COINBASE_EXCHANGE_RATES_URL))
            .willReturn(aResponse()
                .withFixedDelay((int) timeoutInMillis.toMillis())
                .withStatus(200)
                .withHeader(Header.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(COINBASE_RESPONSE_MOCK_JSON_FILE)));

        // WHEN + THEN
        bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(Currency.USD)
            .expectStatus().isEqualTo(HttpStatus.REQUEST_TIMEOUT.code())
            .expectBody()
            .jsonPath("$.path").isEqualTo("/api/v2/bitcoin")
            .jsonPath("$.errors[0].message").isEqualTo("The request timed out due to a downstream operation took too long to complete.");
    }
}
