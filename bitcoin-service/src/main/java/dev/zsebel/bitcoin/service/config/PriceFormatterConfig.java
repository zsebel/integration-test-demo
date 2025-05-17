package dev.zsebel.bitcoin.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "price-formatter-configuration")
public record PriceFormatterConfig(List<LanguageTagConfig> languageTagConfigs) {

    public record LanguageTagConfig(String currency, String languageTag) { }
}
