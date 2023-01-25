package com.dimitris.finapp.dto;

import com.dimitris.finapp.dto.transaction.TransactionRequestDto;

import java.util.Objects;

/**
 * DTO class used for exception responses by REST endpoint {@link com.dimitris.finapp.controllers.TransactionController#executeTransaction(TransactionRequestDto)}
 */
public class FinErrorResponseDto {

    private String errorMessage = null;

    public FinErrorResponseDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinErrorResponseDto that = (FinErrorResponseDto) o;
        return Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }

    @Override
    public String toString() {
        return "FinErrorResponseDto{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
