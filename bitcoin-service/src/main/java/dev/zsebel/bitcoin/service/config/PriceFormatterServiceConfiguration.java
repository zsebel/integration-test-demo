package dev.zsebel.bitcoin.service.config;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.zsebel.bitcoin.service.PriceFormatterService;
import dev.zsebel.bitcoin.service.config.PriceFormatterConfig.LanguageTagConfig;

@Configuration
public class PriceFormatterServiceConfiguration {

    @Bean
    public Map<String, Locale> supportedLocalesByCurrency(final PriceFormatterConfig priceFormatterConfig) {
        return priceFormatterConfig.languageTagConfigs()
            .stream()
            .collect(Collectors.toMap(LanguageTagConfig::currency, config -> Locale.forLanguageTag(config.languageTag())));
    }

    @Bean
    public PriceFormatterService priceFormatterService(final Map<String, Locale> supportedLocalesByCurrency) {
        return new PriceFormatterService(supportedLocalesByCurrency);
    }
}
