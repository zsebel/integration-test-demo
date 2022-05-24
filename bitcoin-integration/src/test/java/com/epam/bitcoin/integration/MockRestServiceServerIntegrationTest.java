package com.epam.bitcoin.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.epam.bitcoin.BitcoinApplication;
import com.epam.bitcoin.BitcoinResponse;

/**
 * Integration test with mocking downstream CoinDesk client using {@link MockRestServiceServer}.
 *
 * With MockMvc, you're typically setting up a whole web application context and mocking the HTTP requests and responses.
 * So, although a fake DispatcherServlet is up and running, simulating how your MVC stack will function, there are no real network connections made.
 *
 * With RestTemplate, you have to deploy an actual server instance to listen for the HTTP requests you send.
 *
 * Key difference between using MockRestServiceServer and WireMock is that you need to call the local WireMock server instead of using the proper
 * CoinDesk client base url.
 */
@SpringBootTest(classes = BitcoinApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockRestServiceServerIntegrationTest {

    @Value("classpath:/response/coindesk_mock_response.json")
    private Resource mockJsonFile;

    @Value("classpath:/response/manual_mocking_respone.json")
    private Resource bitcoinPricesJsonFile;

    @Autowired
    private FileReader fileReader;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${coindesk.baseUrl}")
    private String baseUrl;

    @Disabled("Run it as a standalone test.")
    @Test
    public void testBitcoinPricesShouldReturnMockedPrices() {
        // GIVEN
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(baseUrl + "/v1/bpi/currentprice.json"))
                .andRespond(MockRestResponseCreators.withSuccess(mockJsonFile, MediaType.APPLICATION_JSON));

        // WHEN
        BitcoinResponse actualResponse = this.testRestTemplate.getForObject("http://localhost:" + port + "/api/bitcoin/prices", BitcoinResponse.class);

        // THEN
        mockRestServiceServer.verify();
        BitcoinResponse expectedResponse = fileReader.read(bitcoinPricesJsonFile, BitcoinResponse.class);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}
