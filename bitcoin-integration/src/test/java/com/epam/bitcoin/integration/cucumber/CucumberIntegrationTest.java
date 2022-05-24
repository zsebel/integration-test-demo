package com.epam.bitcoin.integration.cucumber;

import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.epam.bitcoin.BitcoinApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

@Suite
@CucumberContextConfiguration
@ActiveProfiles("wiremock")
@SelectClasspathResource("com/epam/integration")
@SpringBootTest(classes = BitcoinApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberIntegrationTest {

    private WireMockServer wireMockServer;

    @Before
    public void init() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(8089)
                .withRootDirectory("src/main/resources/wiremock"));

        WireMock.configureFor(8089);
        wireMockServer.start();
    }

    @After
    public void shutDown() {
        wireMockServer.stop();
    }

}
