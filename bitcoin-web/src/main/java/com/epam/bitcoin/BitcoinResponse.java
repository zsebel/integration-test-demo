package com.epam.bitcoin;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class BitcoinResponse {

    private Map<String, Float> prices;

    public BitcoinResponse() {
    }

    public BitcoinResponse(Map<String, Float> prices) {
        this.prices = prices;
    }

    public void setPrices(Map<String, Float> prices) {
        this.prices = prices;
    }

    public Map<String, Float> getPrices() {
        return prices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitcoinResponse that = (BitcoinResponse) o;
        return prices.equals(that.prices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prices);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BitcoinResponse.class.getSimpleName() + "[", "]")
                .add("prices=" + prices)
                .toString();
    }
}
