package com.epam.bitcoin.client;

import java.util.Optional;

import com.epam.bitcoin.domain.CoinDeskResponse;

public interface BitcoinClient {

    Optional<CoinDeskResponse> fetchPrices();

}
