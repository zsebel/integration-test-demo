package com.epam.bitcoin.integration;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.epam.bitcoin.client.BitcoinClient;
import com.epam.bitcoin.domain.CoinDeskResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Primary
@Profile("integration")
public class BitcoinRestClientMock implements BitcoinClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinRestClientMock.class);

    @Value("classpath:/response/coindesk_mock_response.json")
    private Resource coinDeskMockResponse;

    @Autowired
    private ObjectMapper objectMapper;

    public Optional<CoinDeskResponse> fetchPrices() {
        CoinDeskResponse coinDeskResponse = null;
        try {
            LOGGER.info("==> Reading mock CoinDesk response from " + coinDeskMockResponse);
            coinDeskResponse = objectMapper.readValue(coinDeskMockResponse.getInputStream(), CoinDeskResponse.class);
            LOGGER.info("<== The following mocked response was returned: " + objectMapper.writeValueAsString(coinDeskResponse));
        } catch (IOException exception) {
            LOGGER.error("Failed to read mock response from file.");
        }
        return Optional.ofNullable(coinDeskResponse);
    }
}
