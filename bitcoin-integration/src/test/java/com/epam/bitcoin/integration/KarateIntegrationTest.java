package com.epam.bitcoin.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.epam.bitcoin.BitcoinApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.intuit.karate.junit5.Karate;

@ActiveProfiles("wiremock")
@SpringBootTest(classes = BitcoinApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KarateIntegrationTest {

    private static final String LOCAL_SERVER_PORT_PROPERTY_KEY = "local.server.port";

    @LocalServerPort
    private int port;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void init() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(8089)
                .withRootDirectory("src/test/resources/wiremock"));

        WireMock.configureFor(8089);
        wireMockServer.start();
    }

    @BeforeEach
    public void registerMockBehaviour() {
        stubFor(WireMock.get(urlMatching("/v1/bpi/currentprice.json"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("coindesk_mock_response.json")));
    }

    @Karate.Test
    Karate runAllKarateScenarios() {
        return Karate.run("classpath:karate/bitcoin_prices.feature")
                .systemProperty(LOCAL_SERVER_PORT_PROPERTY_KEY, String.valueOf(port));
    }

    @AfterAll
    public static void shutDown() {
        wireMockServer.stop();
    }

}
