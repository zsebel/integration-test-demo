package com.epam.bitcoin.service;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class PriceFormatterService {

    private final Map<String, Locale> supportedLocalesByCurrency;

    public PriceFormatterService(final Map<String, Locale> supportedLocalesByCurrency) {
        this.supportedLocalesByCurrency = supportedLocalesByCurrency;
    }

    public String format(final String bitcoinPriceIndex, final String currency) {
        Locale locale = supportedLocalesByCurrency.get(currency);
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(Double.parseDouble(bitcoinPriceIndex));
    }

}
