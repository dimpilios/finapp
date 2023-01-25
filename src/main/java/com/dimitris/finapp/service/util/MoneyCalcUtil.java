package com.dimitris.finapp.service.util;

import com.dimitris.finapp.exception.FinException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class providing money operations like addition, subtraction, etc
 */
public class MoneyCalcUtil {

    /**
     * Adds two amounts after scaling them to two decimal places. Result is also scaled to two decimal places
     * @param amount1
     * @param amount2
     * @return
     * @throws FinException If any input amount is null
     */
    public static BigDecimal add(BigDecimal amount1, BigDecimal amount2) throws FinException {
        if (amount1 == null || amount2 == null) {
            throw new FinException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed: invalid amount");
        }
        BigDecimal scaledAmount1 = scale(amount1);
        BigDecimal scaledAmount2 = scale(amount2);
        BigDecimal result = scaledAmount1.add(scaledAmount2);
        return scale(result);
    }

    /**
     * Subtracts two amounts after scaling them to two decimal places. Result is also scaled to two decimal places
     * @param amount1
     * @param amount2
     * @return
     * @throws FinException If any input amount is null
     */
    public static BigDecimal subtract(BigDecimal amount1, BigDecimal amount2) throws FinException {
        if (amount1 == null || amount2 == null) {
            throw new FinException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed: invalid amount");
        }
        BigDecimal scaledAmount1 = scale(amount1);
        BigDecimal scaledAmount2 = scale(amount2);
        BigDecimal result = scaledAmount1.subtract(scaledAmount2);
        return scale(result);
    }

    /**
     * Adds an amount with a given factor after scaling amount to two decimal places. Result is also scaled to
     * two decimal places
     * @param amount
     * @return
     * @throws FinException If input amount is null
     */
    public static BigDecimal multiply(BigDecimal amount, double factor) throws FinException {
        if (amount == null) {
            throw new FinException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed: invalid amount");
        }
        BigDecimal scaledAmount = scale(amount);
        BigDecimal decimalFactor = new BigDecimal("" + factor + "");
        BigDecimal result = scaledAmount.multiply(decimalFactor);
        return scale(result);
    }

    /**
     * Compares two amounts after scaling them to two decimal places. Result is also scaled to two decimal places
     * @param amount1
     * @param amount2
     * @return An integer value indicating whether the first amount is bigger, equal to or smaller than the second one
     * @throws FinException If any input amount is null
     */
    public static int compare(BigDecimal amount1, BigDecimal amount2) throws FinException {
        if (amount1 == null || amount2 == null) {
            throw new FinException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed: invalid amount");
        }
        BigDecimal scaledAmount1 = scale(amount1);
        BigDecimal scaledAmount2 = scale(amount2);
        return scaledAmount1.compareTo(scaledAmount2);
    }

    /**
     * Scales an amount to two decimal places
     * @param amount
     * @return
     * @throws FinException If input amount is null
     */
    public static BigDecimal scale(BigDecimal amount) throws FinException {
        if (amount == null) {
            throw new FinException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed: invalid amount");
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
