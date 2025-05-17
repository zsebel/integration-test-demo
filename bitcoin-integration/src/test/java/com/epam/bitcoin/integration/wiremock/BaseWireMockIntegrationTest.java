package com.epam.bitcoin.integration.wiremock;

import com.epam.bitcoin.integration.base.BaseIntegrationTest;
import com.epam.bitcoin.integration.model.Header;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public abstract class BaseWireMockIntegrationTest extends BaseIntegrationTest {
    protected static final String COINBASE_RESPONSE_MOCK_JSON_FILE = "coinbase_exchange_rates_response.json";
    protected static final String COINBASE_EXCHANGE_RATES_URL = "/v2/exchange-rates?currency=BTC";

    protected WireMockServer wireMockServer;

    @BeforeAll
    void setupWireMock(
        @Value("${wiremock.port}") Integer wireMockPort,
        @Value("${wiremock.rootDirectory}") String wireMockRootDirectory
    ) {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(wireMockPort)
                .withRootDirectory(wireMockRootDirectory));
            WireMock.configureFor(wireMockPort);
            wireMockServer.start();
        }
    }

    @AfterAll
    void tearDownWireMock() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }

    protected void setupCoinbaseExchangeRatesSuccessStubWith(final String jsonFile) {
        stubFor(WireMock.get(urlEqualTo(COINBASE_EXCHANGE_RATES_URL))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(Header.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(jsonFile)));
    }

    protected void setupCoinbaseExchangeRatesServerErrorStub() {
        stubFor(WireMock.get(urlEqualTo(COINBASE_EXCHANGE_RATES_URL))
            .willReturn(serverError()));
    }
}
