package com.dimitris.finapp.dto.transaction;

import com.dimitris.finapp.persistence.entities.CurrencyType;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO class used to project part of account data, used for service internal purposes
 */
public class AccountBasicsDto {

    private BigDecimal balance;

    private CurrencyType currencyType;

    public AccountBasicsDto(BigDecimal balance, CurrencyType currencyType) {
        this.balance = balance;
        this.currencyType = currencyType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBasicsDto that = (AccountBasicsDto) o;
        return Objects.equals(balance, that.balance) && Objects.equals(currencyType, that.currencyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, currencyType);
    }

    @Override
    public String toString() {
        return "AccountBasicsDto{" +
                "balance=" + balance +
                ", currencyType=" + currencyType +
                '}';
    }
}
