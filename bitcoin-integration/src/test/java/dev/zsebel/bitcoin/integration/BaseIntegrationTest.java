package dev.zsebel.bitcoin.integration;

import dev.zsebel.bitcoin.BitcoinApplication;
import dev.zsebel.bitcoin.integration.support.BitcoinPriceIndexApiTestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    classes = BitcoinApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseIntegrationTest {

    protected static final String BITCOIN_PRICE_INDEX_JSON_PATH = "$.bitcoinPriceIndex";

    @Autowired
    private WebTestClient webTestClient;

    protected BitcoinPriceIndexApiTestClient bitcoinPriceIndexApiTestClient;

    @BeforeAll
    public void init() {
        bitcoinPriceIndexApiTestClient = new BitcoinPriceIndexApiTestClient(webTestClient);
    }
}
