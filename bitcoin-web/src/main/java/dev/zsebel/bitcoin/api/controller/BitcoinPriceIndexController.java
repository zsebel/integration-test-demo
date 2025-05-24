package dev.zsebel.bitcoin.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import dev.zsebel.bitcoin.api.model.BitcoinPriceIndexResponse;
import dev.zsebel.bitcoin.api.validation.annotation.SupportedCurrency;
import dev.zsebel.bitcoin.service.BitcoinPriceIndexService;

@Validated
@RestController
public class BitcoinPriceIndexController {

    private final BitcoinPriceIndexService bitcoinPriceIndexService;

    @Autowired
    BitcoinPriceIndexController(final BitcoinPriceIndexService bitcoinPriceIndexService) {
        this.bitcoinPriceIndexService = bitcoinPriceIndexService;
    }

    @GetMapping("api/v2/bitcoin")
    public Mono<ResponseEntity<BitcoinPriceIndexResponse>> fetchExchangeRates(@RequestParam @SupportedCurrency final String currency) {
        return bitcoinPriceIndexService.fetchBitcoinPriceIndex(currency)
            .map(this::mapToResponseEntity);
    }

    private ResponseEntity<BitcoinPriceIndexResponse> mapToResponseEntity(final String bitcoinPriceIndex) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new BitcoinPriceIndexResponse(bitcoinPriceIndex));
    }
}
