package dev.zsebel.bitcoin.integration.cucumber.steps;

import dev.zsebel.bitcoin.integration.client.BitcoinPriceIndexApiTestClient;
import dev.zsebel.bitcoin.integration.model.Currency;
import dev.zsebel.bitcoin.integration.model.Header;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BitcoinPriceIndexSteps {

    protected static final String COINBASE_RESPONSE_MOCK_JSON_FILE = "coinbase_exchange_rates_response.json";
    protected static final String BITCOIN_PRICE_INDEX_JSON_PATH = "$.bitcoinPriceIndex";

    @Autowired
    private WebTestClient webTestClient;
    private BitcoinPriceIndexApiTestClient bitcoinPriceIndexApiTestClient;
    private WebTestClient.ResponseSpec actualResponse;

    @Before
    public void setup() {
        this.bitcoinPriceIndexApiTestClient = new BitcoinPriceIndexApiTestClient(webTestClient);
    }

    @Given("A Coinbase mock response containing Bitcoin exchange rates")
    public void givenACoinbaseMockResponseContainingBitcoinExchangeRates() {
        stubFor(WireMock.get(urlEqualTo("/v2/exchange-rates?currency=BTC"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(Header.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(COINBASE_RESPONSE_MOCK_JSON_FILE)));
    }

    @ParameterType("USD|EUR|GBP|CHF|HUF")
    public Currency Currency(String currency) {
        return Currency.valueOf(currency);
    }

    @When("I fetch BitcoinPriceIndex in {Currency} currency")
    public void whenIFetchBitcoinPriceIndexIn(final Currency currency) {
        actualResponse = bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(currency);
    }

    @Then("The formatted Bitcoin's price should be {string}")
    public void thenTheFormattedBitcoinsPriceShouldBe(final String formattedPrice) {
        actualResponse
            .expectStatus().isOk()
            .expectBody()
            .jsonPath(BITCOIN_PRICE_INDEX_JSON_PATH).isEqualTo(formattedPrice);
    }
}
