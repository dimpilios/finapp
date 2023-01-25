package com.dimitris.finapp.persistence.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * JPA entity class mapping transaction data
 */
@Entity
@Table(name = "transaction", indexes = {
        @Index(name = "transaction_source_account_id", columnList = "source_account_id"),
        @Index(name = "transaction_target_account_id", columnList = "target_account_id"),
        @Index(name = "transaction_source_account_id_target_account_id", columnList = "source_account_id, target_account_id"),
        @Index(name = "transaction_currency_type_id", columnList = "currency_type_id")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false, updatable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id", nullable = false, updatable = false)
    private Account targetAccount;

    @Column(precision = 20, scale = 2, nullable = false, updatable = false)
    private BigDecimal amount;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "currency_type_id", nullable = false, updatable = false)
    private CurrencyType currencyType;

    public Transaction() {
    }

    public Transaction(Account sourceAccount, Account targetAccount, BigDecimal amount, CurrencyType currencyType) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.currencyType = currencyType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(sourceAccount, that.sourceAccount) && Objects.equals(targetAccount, that.targetAccount) && Objects.equals(amount, that.amount) && Objects.equals(currencyType, that.currencyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceAccount, targetAccount, amount, currencyType);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sourceAccount=" + sourceAccount.getId() +
                ", targetAccount=" + targetAccount.getId() +
                ", amount=" + amount +
                ", currency=" + currencyType +
                '}';
    }

    public String toStringWithSourceOnly() {
        return "Transaction{" +
                "id=" + id +
                ", sourceAccount=" + sourceAccount.getId() +
                ", amount=" + amount +
                ", currency=" + currencyType +
                '}';
    }

    public String toStringWithTargetOnly() {
        return "Transaction{" +
                "id=" + id +
                ", targetAccount=" + targetAccount.getId() +
                ", amount=" + amount +
                ", currency=" + currencyType +
                '}';
    }
}
