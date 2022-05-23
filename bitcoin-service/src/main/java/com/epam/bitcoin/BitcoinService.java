package com.epam.bitcoin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.epam.bitcoin.client.BitcoinClient;
import com.epam.bitcoin.domain.CoinDeskResponse;

@Service
public class BitcoinService {

    private final BitcoinClient bitcoinClient;

    BitcoinService(BitcoinClient bitcoinClient) {
        this.bitcoinClient = bitcoinClient;
    }

    public Map<String, Float> getPrices() {
        Map<String, Float> bitcoinPrices = new HashMap<>();
        Optional<CoinDeskResponse> coinDeskResponse = bitcoinClient.fetchPrices();
        if (coinDeskResponse.isPresent()) {
            bitcoinPrices = coinDeskResponse.get().getBpi()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getRate_float()));

        }
        return bitcoinPrices;
    }
}
