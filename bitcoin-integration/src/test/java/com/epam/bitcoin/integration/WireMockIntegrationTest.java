package com.epam.bitcoin.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import com.epam.bitcoin.BitcoinApplication;
import com.epam.bitcoin.BitcoinResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

/**
 * Integration test using WireMock to mock downstream CoinDesk API.
 *
 * You need to call the local WireMock server instead of using the actual CoinDesk client base url.
 * Therefore, a new wiremock profile has been created.
 *
 */
@ActiveProfiles("wiremock")
@SpringBootTest(classes = BitcoinApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WireMockIntegrationTest {

    @Value("classpath:/response/mock_bitcoin_prices.json")
    private Resource mockJsonFile;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FileReader fileReader;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void init() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(8089)
                .withRootDirectory("src/test/resources/wiremock"));

        WireMock.configureFor(8089);
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testBitcoinPricesShouldReturnMockPricesWhenUsingWireMock() {
        // GIVEN
        stubFor(WireMock.get(urlMatching("/v1/bpi/currentprice.json"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("coindesk_mock_response.json")));

        // WHEN
        BitcoinResponse actualResponse = this.testRestTemplate.getForObject("http://localhost:" + port + "/api/bitcoin/prices", BitcoinResponse.class);

        // THEN
        BitcoinResponse expected = fileReader.read(mockJsonFile, BitcoinResponse.class);
        Assertions.assertEquals(expected, actualResponse);
    }

}
