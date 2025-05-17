package com.epam.bitcoin.integration.karate;

import com.epam.bitcoin.BitcoinApplication;
import com.epam.bitcoin.integration.wiremock.BaseWireMockIntegrationTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * and CoinbaseClient requests are sent to a {@link WireMockServer} running on localhost
 * and test scenarios are written in feature files using Gherkin language,
 * but you don't need to create step definitions. (in most cases)
 */
@SpringBootTest(
    classes = BitcoinApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
public class KarateWithWireMockIntegrationTest extends BaseWireMockIntegrationTest {

    private static final String KARATE_PORT_SYSTEM_PROPERTY_KEY = "karate.port";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void registerMockBehaviour() {
        setupCoinbaseExchangeRatesSuccessStubWith(COINBASE_RESPONSE_MOCK_JSON_FILE);
    }

    @Karate.Test
    Karate runAllKarateScenarios() {
        return Karate.run("classpath:karate/bitcoin_price_index_karate.feature")
            .systemProperty(KARATE_PORT_SYSTEM_PROPERTY_KEY, String.valueOf(port));
    }
}
