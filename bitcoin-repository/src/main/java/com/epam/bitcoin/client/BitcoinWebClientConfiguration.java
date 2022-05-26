package com.epam.bitcoin.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class BitcoinWebClientConfiguration {

    @Value("${coindesk.baseUrl}")
    private String baseUrl;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ExchangeStrategies exchangeStrategies(ObjectMapper objectMapper) {
        MediaType mediaType = new MediaType("application", "javascript");
        return ExchangeStrategies
                .builder()
                .codecs(clientDefaultCodecsConfigurer -> {
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, mediaType));
                    clientDefaultCodecsConfigurer.customCodecs().register(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, mediaType));
                    clientDefaultCodecsConfigurer.customCodecs().register(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                }).build();
    }

    @Bean
    public WebClient webClient(ExchangeStrategies exchangeStrategies) {
        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    @Bean
    public BitcoinWebClient bitcoinRestClient(WebClient webClient, ObjectMapper objectMapper) {
        return new BitcoinWebClient(baseUrl, webClient, objectMapper);
    }
}
