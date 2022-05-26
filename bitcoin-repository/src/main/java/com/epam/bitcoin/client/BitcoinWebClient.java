package com.epam.bitcoin.client;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;

import com.epam.bitcoin.domain.CoinDeskResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BitcoinWebClient implements BitcoinClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinWebClient.class);
    private static final String BITCOIN_PRINCE_INDEX_ENDPOINT = "/v1/bpi/currentprice.json";

    private final String baseUrl;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    BitcoinWebClient(String baseUrl, WebClient webClient, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl;
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public Optional<CoinDeskResponse> fetchPrices() {
        CoinDeskResponse coinDeskResponse = null;
        try {
            URI uri = URI.create(baseUrl + BITCOIN_PRINCE_INDEX_ENDPOINT);
            LOGGER.info("==> Sending a GET request to " + uri + " to fetch Bitcoin prices.");
            coinDeskResponse = webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(CoinDeskResponse.class)
                    .block();
            LOGGER.info("<== The following response was returned: " + objectMapper.writeValueAsString(coinDeskResponse));
        } catch (RestClientException exception) {
            LOGGER.error("The following error occurred during CoinDesk call: " + exception.getMessage());
        } catch (JsonProcessingException exception) {
            LOGGER.error("An error occurred during payload processing: " + exception);
        }
        return Optional.ofNullable(coinDeskResponse);
    }
}
