package com.epam.bitcoin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BitcoinController {

    private final BitcoinService bitcoinService;

    @Autowired
    BitcoinController(BitcoinService bitcoinService) {
        this.bitcoinService = bitcoinService;
    }

    @GetMapping("/api/bitcoin/prices")
    public Map<String, Float> fetchPrice() {
        return bitcoinService.getPrices();
    }
}
