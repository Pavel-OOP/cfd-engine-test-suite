package com.cfdengine.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MarginCalculatorTest {

    private MarginCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new MarginCalculator();
    }

    @Test
    @DisplayName("1 lot EURUSD at 1.1050, leverage 1:30 = 3683.33")
    void requiredMargin_oneLotEurusd() {
        BigDecimal result = calculator.requiredMargin(
                new BigDecimal("1"),
                100000,
                new BigDecimal("1.1050"),
                30
        );
        assertEquals(new BigDecimal("3683.33"), result);
    }

    @Test
    @DisplayName("0.01 micro lot precision check")
    void requiredMargin_microLot() {
        BigDecimal result = calculator.requiredMargin(
                new BigDecimal("0.01"),
                100000,
                new BigDecimal("1.1050"),
                30
        );
        assertEquals(new BigDecimal("36.83"), result);
    }

    @Test
    @DisplayName("free margin = equity - used margin")
    void freeMargin_basic() {
        BigDecimal result = calculator.freeMargin(
                new BigDecimal("10000"),
                new BigDecimal("3683.33")
        );
        assertEquals(new BigDecimal("6316.67"), result);
    }

    @Test
    @DisplayName("margin level 150% when equity is 1.5x used margin")
    void marginLevel_healthy() {
        BigDecimal result = calculator.marginLevel(
                new BigDecimal("6000"),   // equity
                new BigDecimal("4000")    // used margin
        );
        // (6000 / 4000) * 100 = 150.00
        assertEquals(new BigDecimal("150.00"), result);
    }

    @Test
    @DisplayName("margin level with zero used margin returns very large number")
    void marginLevel_noPositions() {
        BigDecimal result = calculator.marginLevel(
                new BigDecimal("10000"),
                BigDecimal.ZERO
        );
        assertTrue(result.compareTo(new BigDecimal("100000")) > 0);
    }

    @Test
    @DisplayName("negative free margin after large loss")
    void freeMargin_negative() {
        BigDecimal result = calculator.freeMargin(
                new BigDecimal("2000"),    // equity dropped due to losses
                new BigDecimal("3683.33")  // margin still locked
        );
        // 2000 - 3683.33 = -1683.33
        assertEquals(new BigDecimal("-1683.33"), result);
    }

    @ParameterizedTest
    @DisplayName("required margin across various instruments and leverage")
    @CsvSource({
            "1,    100000, 1.1050, 30,  3683.33",
            "0.01, 100000, 1.1050, 30,  36.83",
            "1,    100000, 1.1050, 100, 1105.00",
            "0.5,  100000, 145.50, 20,  363750.00"
    })
    void requiredMargin_variousInputs(
            BigDecimal lot, int contract,
            BigDecimal price, int lev, BigDecimal expected) {
        BigDecimal result = calculator.requiredMargin(lot, contract, price, lev);
        assertEquals(expected, result);
    }
}