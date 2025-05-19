package dev.zsebel.bitcoin.integration.stub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zsebel.bitcoin.integration.stub.client.CoinbaseClientStub;
import dev.zsebel.bitcoin.integration.stub.util.ClasspathFileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

@TestConfiguration
public class CoinbaseClientStubConfiguration {

    @Value("classpath:/response/coinbase/coinbase_exchange_rates_response.json")
    private Resource coinbaseResponseJsonFile;

    @Bean
    public ClasspathFileReader classpathFileReader(final ObjectMapper objectMapper) {
        return new ClasspathFileReader(objectMapper);
    }

    @Bean
    @Primary
    @Profile("stub")
    public CoinbaseClientStub coinbaseClientStub(final ClasspathFileReader classpathFileReader) {
        return new CoinbaseClientStub(classpathFileReader, coinbaseResponseJsonFile);
    }

}
