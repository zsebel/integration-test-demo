package com.epam.bitcoin.integration.cucumber.steps;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;

import com.epam.bitcoin.BitcoinResponse;
import com.epam.bitcoin.integration.FileReader;
import com.github.tomakehurst.wiremock.client.WireMock;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BitcoinStepDefs {

    @Value("classpath:/response/manual_mocking_respone.json")
    private Resource mockJsonFile;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FileReader fileReader;

    private BitcoinResponse actualResponse;

    @Given("Bitcoin price is above 100k")
    public void bitcoinPriceIsAbove100K() {
        stubFor(WireMock.get(urlMatching("/v1/bpi/currentprice.json"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("coindesk_mock_response.json")));
    }

    @When("I fetch Bitcoin prices")
    public void fetchBitcoinPrices() {
        actualResponse = this.testRestTemplate.getForObject("http://localhost:" + 8080 + "/api/bitcoin/prices", BitcoinResponse.class);
    }

    @Then("Bitcoin price should be more than 100k")
    public void bitcoinPriceShouldBeMoreThan100K() {
        BitcoinResponse expectedResponse = fileReader.read(mockJsonFile, BitcoinResponse.class);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Then("Bitcoin price should be {string} in {string}")
    public void bitcoinPriceShouldBeIn(String amount, String currency) {
        Map<String, Float> prices = actualResponse.getPrices();
        Assertions.assertEquals(String.valueOf(prices.get(currency)), String.valueOf(amount));
    }
}
