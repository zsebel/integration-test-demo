package com.epam.bitcoin.integration;

import org.junit.jupiter.api.Assertions;
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

/**
 * Basic integration test using {@link TestRestTemplate} with mocking downstream CoinDesk client.
 *
 * This approach starts:
 *  - Root WebApplicationContext
 *  - Embedded Tomcat server
 *  - Spring DispatcherServer
 */
@SpringBootTest(classes = BitcoinApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public class ManualMockingIntegrationTest {

    @Value("classpath:/response/manual_mocking_respone.json")
    private Resource expectedJsonResponse;

    @Autowired
    private FileReader fileReader;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testBitcoinPricesShouldReturnMockedPrices() {
        // GIVEN

        // WHEN
        BitcoinResponse actual = this.testRestTemplate.getForObject("http://localhost:" + port + "/api/bitcoin/prices", BitcoinResponse.class);

        // THEN
        BitcoinResponse expected = fileReader.read(expectedJsonResponse, BitcoinResponse.class);
        Assertions.assertEquals(expected, actual);
    }
}
