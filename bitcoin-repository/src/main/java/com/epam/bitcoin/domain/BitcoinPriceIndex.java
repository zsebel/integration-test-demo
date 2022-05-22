package com.epam.bitcoin.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class BitcoinPriceIndex {
    private String code;
    private String symbol;
    private String rate;
    private String description;
    private float rate_float;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRate_float() {
        return rate_float;
    }

    public void setRate_float(float rate_float) {
        this.rate_float = rate_float;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitcoinPriceIndex that = (BitcoinPriceIndex) o;
        return Float.compare(that.rate_float, rate_float) == 0 && code.equals(that.code) && symbol.equals(that.symbol) && rate.equals(that.rate) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, symbol, rate, description, rate_float);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BitcoinPriceIndex.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("symbol='" + symbol + "'")
                .add("rate='" + rate + "'")
                .add("description='" + description + "'")
                .add("rate_float=" + rate_float)
                .toString();
    }
}
