package com.epam.bitcoin.service.config;

import com.epam.bitcoin.service.PriceFormatterService;
import com.epam.bitcoin.service.config.PriceFormatterConfig.LanguageTagConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
