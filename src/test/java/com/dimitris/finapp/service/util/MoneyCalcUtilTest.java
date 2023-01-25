package com.dimitris.finapp.service.util;

import com.dimitris.finapp.exception.FinException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyCalcUtilTest {

    private static Stream<Arguments> provideInputAndOutputForTestScale() {
        return Stream.of(
                Arguments.of(new BigDecimal("0.990"), new BigDecimal("0.99")),
                Arguments.of(new BigDecimal("0.995"), new BigDecimal("1.00")),
                Arguments.of(new BigDecimal("1"), new BigDecimal("1.00")),
                Arguments.of(new BigDecimal("1.0"), new BigDecimal("1.00")),
                Arguments.of(new BigDecimal("1.00"), new BigDecimal("1.00")),
                Arguments.of(new BigDecimal("1.000"), new BigDecimal("1.00")),
                Arguments.of(new BigDecimal("1.005"), new BigDecimal("1.01"))
        );
    }

    private static Stream<Arguments> provideInputsAndOutputForTestMultiply() {
        return Stream.of(
                Arguments.of(new BigDecimal("8.25"), 0, new BigDecimal("0.00")),
                Arguments.of(new BigDecimal("8.25"), 0.189, new BigDecimal("1.56"))
        );
    }

    private static Stream<Arguments> provideInputsAndOutputForTestCompare() {
        return Stream.of(
                Arguments.of(new BigDecimal("0.00"), new BigDecimal("0.00"), 0),
                Arguments.of(new BigDecimal("1.29"), new BigDecimal("0.00"), 1),
                Arguments.of(new BigDecimal("1.29"), new BigDecimal("1.31"), -1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputAndOutputForTestScale")
    public void testScale(BigDecimal inputAmount, BigDecimal scaledAmount) throws FinException {
        BigDecimal result = MoneyCalcUtil.scale(inputAmount);
        assertEquals(result, scaledAmount);
    }

    @ParameterizedTest
    @NullSource
    public void testScale_NullInput(BigDecimal inputAmount) {
        assertThrows(FinException.class, () -> MoneyCalcUtil.scale(inputAmount));
    }

    @Test
    public void testAdd() throws FinException {
        BigDecimal result = MoneyCalcUtil.add(new BigDecimal("0.23"), new BigDecimal("0.02"));
        assertEquals(result, new BigDecimal("0.25"));
    }

    @ParameterizedTest
    @NullSource
    public void testAdd_NullInput(BigDecimal inputAmount) {
        assertThrows(FinException.class, () -> MoneyCalcUtil.add(inputAmount, new BigDecimal("0.02")));
    }

    @Test
    public void testSubtract() throws FinException {
        BigDecimal result = MoneyCalcUtil.subtract(new BigDecimal("0.23"), new BigDecimal("0.02"));
        assertEquals(result, new BigDecimal("0.21"));
    }

    @ParameterizedTest
    @NullSource
    public void testSubtract_NullInput(BigDecimal inputAmount) {
        assertThrows(FinException.class, () -> MoneyCalcUtil.subtract(inputAmount, new BigDecimal("0.02")));
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputForTestMultiply")
    public void testMultiply(BigDecimal inputAmount, double factor, BigDecimal product) throws FinException {
        BigDecimal result = MoneyCalcUtil.multiply(inputAmount, factor);
        assertEquals(result, product);
    }

    @ParameterizedTest
    @NullSource
    public void testMultiply_NullInput(BigDecimal inputAmount) {
        assertThrows(FinException.class, () -> MoneyCalcUtil.multiply(inputAmount, 0.18));
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputForTestCompare")
    public void testCompare(BigDecimal inputAmount1, BigDecimal inputAmount2, int comparisonValue) throws FinException {
        int result = MoneyCalcUtil.compare(inputAmount1, inputAmount2);
        assertEquals(result, comparisonValue);
    }

    @ParameterizedTest
    @NullSource
    public void testCompare_NullInput(BigDecimal inputAmount) {
        assertThrows(FinException.class, () -> MoneyCalcUtil.compare(inputAmount, new BigDecimal("0.1")));
    }
}
