package dev.zsebel.bitcoin.client.config;

import java.time.Duration;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "retry")
public record RetryConfiguration(Map<String, RetryProperties> instances) {

    public record RetryProperties(int maxAttempts, Duration waitDuration) {
    }
}