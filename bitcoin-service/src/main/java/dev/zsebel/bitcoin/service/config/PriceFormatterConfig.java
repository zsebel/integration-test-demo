package dev.zsebel.bitcoin.service.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "price-formatter-configuration")
public record PriceFormatterConfig(List<LanguageTagConfig> languageTagConfigs) {

    public record LanguageTagConfig(String currency, String languageTag) {
    }
}
