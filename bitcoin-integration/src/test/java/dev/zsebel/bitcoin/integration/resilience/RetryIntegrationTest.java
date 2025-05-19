package dev.zsebel.bitcoin.integration.resilience;

import dev.zsebel.bitcoin.integration.support.Currency;
import dev.zsebel.bitcoin.integration.support.Header;
import dev.zsebel.bitcoin.integration.wiremock.BaseWireMockIntegrationTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * and CoinbaseClient requests are sent to a {@link WireMockServer} running on localhost
 * but in this scenario simulates server errors for the first two attempts.
 */
@ActiveProfiles("integration")
public class RetryIntegrationTest extends BaseWireMockIntegrationTest {

    private static final String RETRY_SCENARIO = "Retry scenario";
    private static final String STATE_FIRST_RETRY = "First retry attempt";
    private static final String STATE_SECOND_RETRY = "Second retry attempt";

    @Test
    public void testRetryIntegrationShouldReturnFormattedBitcoinPriceWhenCoinbaseCallIsSuccessfulAfterTwoFailedRetries() {
        // GIVEN
        stubFor(WireMock.get(urlEqualTo(COINBASE_EXCHANGE_RATES_URL))
            .inScenario(RETRY_SCENARIO)
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(serverError())
            .willSetStateTo(STATE_FIRST_RETRY));

        stubFor(WireMock.get(urlEqualTo(COINBASE_EXCHANGE_RATES_URL))
            .inScenario(RETRY_SCENARIO)
            .whenScenarioStateIs(STATE_FIRST_RETRY)
            .willReturn(serverError())
            .willSetStateTo(STATE_SECOND_RETRY));

        stubFor(WireMock.get(urlEqualTo(COINBASE_EXCHANGE_RATES_URL))
            .inScenario(RETRY_SCENARIO)
            .whenScenarioStateIs(STATE_SECOND_RETRY)
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(Header.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(COINBASE_RESPONSE_MOCK_JSON_FILE)));

        // WHEN
        WebTestClient.ResponseSpec actualResponse = bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(Currency.USD);

        // THEN
        actualResponse
            .expectStatus().isOk()
            .expectBody().jsonPath(BITCOIN_PRICE_INDEX_JSON_PATH).isEqualTo("$95,472.37");

        wireMockServer.verify(3, getRequestedFor(urlEqualTo(COINBASE_EXCHANGE_RATES_URL)));
    }
}
