package dev.zsebel.bitcoin.integration.cucumber;

import dev.zsebel.bitcoin.BitcoinApplication;
import dev.zsebel.bitcoin.integration.cucumber.steps.BitcoinPriceIndexSteps;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * and CoinbaseClient requests are sent to a {@link WireMockServer} running on localhost
 * but test scenarios are written in feature files using Gherkin language.
 * Step definitions are declared in {@link BitcoinPriceIndexSteps}
 */
@Suite
@CucumberContextConfiguration
@ActiveProfiles("integration")
@SelectClasspathResource("cucumber")
@SpringBootTest(
    classes = BitcoinApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CucumberIntegrationTestRunner {

    @Value("${wiremock.port}")
    private Integer wireMockPort;

    @Value("${wiremock.rootDirectory}")
    private String wireMockRootDirectory;

    private WireMockServer wireMockServer;

    @Before
    public void init() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
            .port(wireMockPort)
            .withRootDirectory(wireMockRootDirectory));

        WireMock.configureFor(wireMockPort);
        wireMockServer.start();
    }

    @After
    public void shutDown() {
        wireMockServer.stop();
    }
}
