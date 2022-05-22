package com.epam.bitcoin.client;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.epam.bitcoin.domain.CoinDeskResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BitcoinRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinRestClient.class);
    private static final String BITCOIN_PRINCE_INDEX_ENDPOINT = "/v1/bpi/currentprice.json";

    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    BitcoinRestClient(String baseUrl, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Optional<CoinDeskResponse> fetchPrices() {
        CoinDeskResponse coinDeskResponse = null;
        try {
            URI uri = URI.create(baseUrl + BITCOIN_PRINCE_INDEX_ENDPOINT);
            LOGGER.info("==> Sending a GET request to " + uri + " to fetch Bitcoin prices.");
            ResponseEntity<CoinDeskResponse> responseEntity = restTemplate.getForEntity(uri, CoinDeskResponse.class);
            coinDeskResponse = responseEntity.getBody();
            LOGGER.info("<== The following response was returned: " + objectMapper.writeValueAsString(coinDeskResponse));
        } catch (RestClientException exception) {
            LOGGER.error("The following error occurred during CoinDesk call: " + exception.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(coinDeskResponse);
    }
}
