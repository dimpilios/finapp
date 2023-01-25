package com.dimitris.finapp.enums;

/**
 * Enumeration of Currency types. Should be aligned to respective DB stored currencies (ids should be the same)
 */
public enum CurrencyEnum {

    USD(1, "USD"),
    CAD(2, "CAD"),
    HKD(3, "HKD"),
    AUD(4, "AUD"),
    GBP(5, "GBP"),
    EUR(6, "EUR"),
    JPY(7, "JPY");

    public Integer id;
    public String name;

    CurrencyEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
