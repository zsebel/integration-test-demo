package com.epam.bitcoin.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.epam.bitcoin.converter.JavaScriptMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class BitcoinRestClientConfiguration {

    @Value("${coindesk.baseUrl}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate(JavaScriptMessageConverter javaScriptMessageConverter) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(javaScriptMessageConverter);
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BitcoinRestClient bitcoinRestClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new BitcoinRestClient(baseUrl, restTemplate, objectMapper);
    }
}
