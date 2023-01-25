package com.dimitris.finapp.dto.transaction;

import java.util.Objects;

/**
 * DTO class used for responses by REST endpoint {@link com.dimitris.finapp.controllers.TransactionController#executeTransaction(TransactionRequestDto)}
 */
public class TransactionResponseDto {

    private Long transactionId;

    public TransactionResponseDto() {
    }

    public TransactionResponseDto(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionResponseDto that = (TransactionResponseDto) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "TransactionResponseDto{" +
                "transactionId=" + transactionId +
                '}';
    }
}
