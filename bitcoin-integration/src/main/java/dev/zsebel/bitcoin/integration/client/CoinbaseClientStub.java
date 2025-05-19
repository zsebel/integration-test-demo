package dev.zsebel.bitcoin.integration.client;

import dev.zsebel.bitcoin.client.ExchangeRatesClient;
import dev.zsebel.bitcoin.client.model.CoinbaseResponse;
import dev.zsebel.bitcoin.integration.util.ClasspathFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
@Profile("manual")
public class CoinbaseClientStub implements ExchangeRatesClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinbaseClientStub.class);

    private final ClasspathFileReader classpathFileReader;
    private final Resource coinbaseResponseJsonFile;

    @Autowired
    public CoinbaseClientStub(
        final ClasspathFileReader classpathFileReader,
        @Value("classpath:/response/coinbase/coinbase_exchange_rates_response.json")final Resource coinbaseResponseJsonFile
    ) {
        this.classpathFileReader = classpathFileReader;
        this.coinbaseResponseJsonFile = coinbaseResponseJsonFile;
    }

    @Override
    public Mono<CoinbaseResponse> fetchExchangeRates() {
        LOGGER.info("Reading Coinbase response from {} file.", coinbaseResponseJsonFile.getFilename());
        CoinbaseResponse coinbaseResponse = classpathFileReader.read(coinbaseResponseJsonFile, CoinbaseResponse.class);
        LOGGER.info("Successfully read Coinbase response.");
        return Mono.just(coinbaseResponse);
    }
}

