package com.epam.bitcoin.client.model;

import java.util.Map;

public record CoinbaseResponse(Data data) {

    public record Data(
            String currency,
            Map<String, String> rates
    ) { }

}
