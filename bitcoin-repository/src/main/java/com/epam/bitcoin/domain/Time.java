package com.epam.bitcoin.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class Time {
    private String updated;
    private String updatedISO;
    private String updateduk;

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdatedISO() {
        return updatedISO;
    }

    public void setUpdatedISO(String updatedISO) {
        this.updatedISO = updatedISO;
    }

    public String getUpdateduk() {
        return updateduk;
    }

    public void setUpdateduk(String updateduk) {
        this.updateduk = updateduk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return updated.equals(time.updated) && updatedISO.equals(time.updatedISO) && updateduk.equals(time.updateduk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updated, updatedISO, updateduk);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Time.class.getSimpleName() + "[", "]")
                .add("updated='" + updated + "'")
                .add("updatedISO='" + updatedISO + "'")
                .add("updateduk='" + updateduk + "'")
                .toString();
    }
}
