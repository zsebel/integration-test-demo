package dev.zsebel.bitcoin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.zsebel.bitcoin.client.ExchangeRatesClient;

@Service
public class BitcoinPriceIndexService {

    private final ExchangeRatesClient coinbaseExchangeRatesClientWrapper;
    private final PriceFormatterService priceFormatterService;

    @Autowired
    public BitcoinPriceIndexService(final ExchangeRatesClient coinbaseExchangeRatesClientWrapper, final PriceFormatterService priceFormatterService) {
        this.coinbaseExchangeRatesClientWrapper = coinbaseExchangeRatesClientWrapper;
        this.priceFormatterService = priceFormatterService;
    }

    public Mono<String> fetchBitcoinPriceIndex(final String currency) {
        return coinbaseExchangeRatesClientWrapper.fetchExchangeRates()
            .map(coinbaseResponse -> coinbaseResponse.data().rates().get(currency))
            .map(bitcoinPriceIndex -> priceFormatterService.format(bitcoinPriceIndex, currency));
    }
}
