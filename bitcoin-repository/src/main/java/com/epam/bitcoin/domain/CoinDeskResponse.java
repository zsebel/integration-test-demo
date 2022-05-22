package com.epam.bitcoin.domain;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinDeskResponse {
    private Time time;
    private String disclaimer;
    private String chartName;
    private Map<String, BitcoinPriceIndex> bpi;

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Map<String, BitcoinPriceIndex> getBpi() {
        return bpi;
    }

    public void setBpi(Map<String, BitcoinPriceIndex> bpi) {
        this.bpi = bpi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinDeskResponse that = (CoinDeskResponse) o;
        return time.equals(that.time) && disclaimer.equals(that.disclaimer) && chartName.equals(that.chartName) && bpi.equals(that.bpi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, disclaimer, chartName, bpi);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CoinDeskResponse.class.getSimpleName() + "[", "]")
                .add("time=" + time)
                .add("disclaimer='" + disclaimer + "'")
                .add("chartName='" + chartName + "'")
                .add("bpi=" + bpi)
                .toString();
    }
}
