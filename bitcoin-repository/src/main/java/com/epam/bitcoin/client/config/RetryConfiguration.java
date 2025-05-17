package com.epam.bitcoin.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

@ConfigurationProperties(prefix = "retry")
public record RetryConfiguration(Map<String, RetryProperties> instances) {

    public record RetryProperties(int maxAttempts, Duration waitDuration) {}
}