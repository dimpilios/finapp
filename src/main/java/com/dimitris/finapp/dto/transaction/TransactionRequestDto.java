package com.dimitris.finapp.dto.transaction;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO class used for requests by REST endpoint {@link com.dimitris.finapp.controllers.TransactionController#executeTransaction(TransactionRequestDto)}
 */
public class TransactionRequestDto {

    private Long sourceAccountId;

    private Long targetAccountId;

    private BigDecimal amount;

    public TransactionRequestDto() {
    }

    public TransactionRequestDto(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRequestDto that = (TransactionRequestDto) o;
        return Objects.equals(sourceAccountId, that.sourceAccountId) && Objects.equals(targetAccountId, that.targetAccountId) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceAccountId, targetAccountId, amount);
    }

    @Override
    public String toString() {
        return "TransactionRequestDTO{" +
                "sourceAccountId=" + sourceAccountId +
                ", targetAccountId=" + targetAccountId +
                ", amount=" + amount +
                '}';
    }
}
