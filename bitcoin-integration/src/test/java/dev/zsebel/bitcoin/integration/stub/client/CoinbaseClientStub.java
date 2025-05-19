package dev.zsebel.bitcoin.integration.stub.client;

import dev.zsebel.bitcoin.client.ExchangeRatesClient;
import dev.zsebel.bitcoin.client.model.CoinbaseResponse;
import dev.zsebel.bitcoin.integration.stub.util.ClasspathFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class CoinbaseClientStub implements ExchangeRatesClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinbaseClientStub.class);
    private static final long SIMULATED_LATENCY_IN_MILLISECONDS = 10L;

    private final ClasspathFileReader classpathFileReader;
    private final Resource coinbaseResponseJsonFile;

    public CoinbaseClientStub(final ClasspathFileReader classpathFileReader, final Resource coinbaseResponseJsonFile) {
        this.classpathFileReader = classpathFileReader;
        this.coinbaseResponseJsonFile = coinbaseResponseJsonFile;
    }

    @Override
    public Mono<CoinbaseResponse> fetchExchangeRates() {
        LOGGER.info("Reading Coinbase response from {} file.", coinbaseResponseJsonFile.getFilename());
        CoinbaseResponse coinbaseResponse = classpathFileReader.read(coinbaseResponseJsonFile, CoinbaseResponse.class);
        LOGGER.info("Successfully read Coinbase response.");
        return Mono.delay(Duration.ofMillis(SIMULATED_LATENCY_IN_MILLISECONDS)).thenReturn(coinbaseResponse);
    }
}

