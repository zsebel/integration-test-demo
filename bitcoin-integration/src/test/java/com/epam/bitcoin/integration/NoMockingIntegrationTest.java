package com.epam.bitcoin.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.epam.bitcoin.BitcoinApplication;
import com.epam.bitcoin.BitcoinResponse;

/**
 * Basic integration test using {@link TestRestTemplate} without mocking downstream CoinDesk client.
 *
 * This approach starts:
 *  - Root WebApplicationContext
 *  - Embedded Tomcat server
 *  - Spring DispatcherServer
 */
@SpringBootTest(classes = BitcoinApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoMockingIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test() {
        // GIVEN

        // WHEN
        BitcoinResponse actual = this.testRestTemplate.getForObject("http://localhost:" + port + "/api/bitcoin/prices", BitcoinResponse.class);

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.getPrices().size());
    }
}
