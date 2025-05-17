package dev.zsebel.bitcoin.integration.karate;

import dev.zsebel.bitcoin.BitcoinApplication;
import com.intuit.karate.core.MockServer;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * and CoinbaseClient requests are sent to Karate's internal {@link MockServer} running on localhost.
 * Test scenarios are written in feature files using Gherkin language,
 * and in most cases step definitions don't need to be declared.
 */
@SpringBootTest(
    classes = BitcoinApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KarateWithMockServerIntegrationTest {

    private static final String KARATE_PORT_SYSTEM_PROPERTY_KEY = "karate.port";

    private MockServer mockServer;

    @LocalServerPort
    private int port;

    @Karate.Test
    Karate runAll(@Value("${mockServer.port}") int mockServerPort) {
        mockServer = MockServer.feature("classpath:karate/mock/coinbase-mock.feature")
            .http(mockServerPort)
            .build();
        return Karate.run("classpath:karate/bitcoin_price_index_karate_with_mock_server.feature")
            .systemProperty(KARATE_PORT_SYSTEM_PROPERTY_KEY, String.valueOf(port));
    }

    @AfterAll
    public void afterAll() {
        if (mockServer != null) {
            mockServer.stop();
        }
    }
}
