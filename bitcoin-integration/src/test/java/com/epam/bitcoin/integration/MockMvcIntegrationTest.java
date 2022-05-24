package com.epam.bitcoin.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.DispatcherServlet;

import com.epam.bitcoin.BitcoinApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;



/**
 * Integration test using {@link MockMvc} instead of {@link TestRestTemplate}.
 *
 * This approach does NOT start an embedded Tomcat server and uses a subclass of the {@link DispatcherServlet} to handle test requests.
 * The TestDispatcherServlet is responsible for calling controllers. MockMvc wraps this TestDispatcherServlet internally so every time we send a request
 * using perform() method, MockMvc will use TestDispatcherServlet. Therefore, there are no real network connections made.
 *
 * This setup does not support redirections thus we are not able to test certain API failures when Spring Boot redirects the current request to the /error endpoint for instance.
 */
@AutoConfigureMockMvc
@ActiveProfiles("wiremock")
@SpringBootTest(classes = BitcoinApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockMvcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("classpath:/response/mock_mvc_response.json")
    private Resource mockJsonFile;

    @Autowired
    private FileReader fileReader;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void init() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(8089)
                .withRootDirectory("src/main/resources/wiremock"));

        WireMock.configureFor(8089);
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testBitcoinPricesShouldReturnMockPricesWhenUsingWireMock() throws Exception {
        // GIVEN
        stubFor(WireMock.get(urlMatching("/v1/bpi/currentprice.json"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("coindesk_mock_response.json")));

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bitcoin/prices"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(fileReader.readFileToBytes(mockJsonFile)));

        // THEN
    }
}
