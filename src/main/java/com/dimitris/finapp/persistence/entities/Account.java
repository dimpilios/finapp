package com.dimitris.finapp.persistence.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * JPA entity class mapping account data
 */
@Entity
@Table(name = "account", indexes = {
        @Index(name = "account_currency_type_id", columnList = "currency_type_id")
})
public class Account {

    @Id
    private Long id;

    @Column(precision = 20, scale = 2, nullable = false, insertable = false)
    private BigDecimal balance = new BigDecimal("0");

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "currency_type_id", nullable = false, insertable = false, updatable = false)
    private CurrencyType currencyType;

    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.REFRESH)
    private List<Transaction> transactionsAsSource;

    @OneToMany(mappedBy = "targetAccount", cascade = CascadeType.REFRESH)
    private List<Transaction> transactionsAsTarget;

    public Account() {
    }

    public Account(Long id, BigDecimal balance, CurrencyType currencyType, LocalDate createdAt) {
        this.id = id;
        this.balance = balance;
        this.currencyType = currencyType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal salary) {
        this.balance = salary;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<Transaction> getTransactionsAsSource() {
        return transactionsAsSource;
    }

    public void setTransactionsAsSource(List<Transaction> transactionsAsSource) {
        this.transactionsAsSource = transactionsAsSource;
    }

    public List<Transaction> getTransactionsAsTarget() {
        return transactionsAsTarget;
    }

    public void setTransactionsAsTarget(List<Transaction> transactionsAsTarget) {
        this.transactionsAsTarget = transactionsAsTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(balance, account.balance) && Objects.equals(currencyType, account.currencyType) && Objects.equals(createdAt, account.createdAt) && Objects.equals(transactionsAsSource, account.transactionsAsSource) && Objects.equals(transactionsAsTarget, account.transactionsAsTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, currencyType, createdAt, transactionsAsSource, transactionsAsTarget);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", salary=" + balance +
                ", currencyType=" + currencyType +
                ", createdAt=" + createdAt +
                ", transactionsAsSource=" + transactionsAsSource.stream().map(t -> t.toStringWithTargetOnly()).collect(Collectors.toList()) +
                ", transactionsAsTarget=" + transactionsAsTarget.stream().map(t -> t.toStringWithSourceOnly()).collect(Collectors.toList()) +
                '}';
    }
}
