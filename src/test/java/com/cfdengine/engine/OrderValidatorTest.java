package com.cfdengine.engine;

import com.cfdengine.model.MarketOrder;
import com.cfdengine.model.Order;
import com.cfdengine.model.Account;
import com.cfdengine.config.InstrumentConfig;
import com.cfdengine.model.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {

    private OrderValidator validator;
    private Account account;
    private InstrumentConfig config;

    @BeforeEach
    void setUp() {
        validator = new OrderValidator();

        // Standard retail account: €10k balance, 1:30 leverage
        account = new Account(new BigDecimal("10000"), 30);

        // EURUSD config: 2 pip spread, 100k contract, 0.01-100 lots, 1:30 max
        config = new InstrumentConfig(
                "EURUSD",
                new BigDecimal("0.00020"),  // spread
                100000,                      // contract size
                new BigDecimal("0.01"),      // min lot
                new BigDecimal("100"),       // max lot
                new BigDecimal("0.0001"),    // pip size
                30                           // max leverage
        );
    }

    // --- Helper to create a simple market order quickly ---
    private Order makeOrder(BigDecimal size, String instrument) {
        return new MarketOrder(Direction.BUY, size, instrument);
    }

    private Order makeOrder(BigDecimal size) {
        return makeOrder(size, "EURUSD");
    }

    @Test
    @DisplayName("valid order returns empty violations list")
    void validOrder_noViolations() {
        Order order = makeOrder(new BigDecimal("1"));
        List<String> violations = validator.validate(order, account, config);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("size = 0 is rejected")
    void zeroSize_violation() {
        Order order = makeOrder(BigDecimal.ZERO);
        List<String> violations = validator.validate(order, account, config);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("negative size is rejected")
    void negativeSize_violation() {
        Order order = makeOrder(new BigDecimal("-1"));
        List<String> violations = validator.validate(order, account, config);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("size below min lot is rejected")
    void belowMinLot_violation() {
        Order order = makeOrder(new BigDecimal("0.001")); // min is 0.01
        List<String> violations = validator.validate(order, account, config);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("size above max lot is rejected")
    void aboveMaxLot_violation() {
        Order order = makeOrder(new BigDecimal("150")); // max is 100
        List<String> violations = validator.validate(order, account, config);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("null instrument is rejected")
    void nullInstrument_violation() {
        Order order = makeOrder(new BigDecimal("1"), null);
        List<String> violations = validator.validate(order, account, config);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("empty instrument is rejected")
    void emptyInstrument_violation() {
        Order order = makeOrder(new BigDecimal("1"), "");
        List<String> violations = validator.validate(order, account, config);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("multiple violations returned at once")
    void multipleViolations() {
        Order order = makeOrder(new BigDecimal("-1"), "");
        List<String> violations = validator.validate(order, account, config);
        // negative size triggers size > 0 AND size < minLot, plus empty instrument
        assertTrue(violations.size() >= 3,
                "Expected at least 3 violations, got: " + violations);
    }

    @ParameterizedTest
    @DisplayName("size boundary checks")
    @CsvSource({
            "-100,  true",   // way below zero
            "-0.01, true",   // just below zero
            "0,     true",   // zero
            "0.001, true",   // below min lot (0.01)
            "0.009, true",   // just below min lot
            "0.01,  false",  // exact min lot
            "0.5,   false",  // normal size
            "1,     false",  // standard lot
            "100,   false",  // exact max lot
            "100.01,true",   // just above max lot
            "500,   true"    // way above max lot
    })
    void sizeBoundaryChecks(BigDecimal size, boolean shouldHaveViolation) {
        Order order = makeOrder(size);
        List<String> violations = validator.validate(order, account, config);

        if (shouldHaveViolation) {
            assertFalse(violations.isEmpty(),
                    "Expected violation for size " + size + " but got none");
        } else {
            assertTrue(violations.isEmpty(),
                    "Expected no violations for size " + size + " but got: " + violations);
        }
    }

    @Test
    @DisplayName("leverage exceeding instrument max is rejected")
    void leverageExceeded_violation() {
        // Account with 1:50 but instrument only allows 1:30
        Account highLevAccount = new Account(new BigDecimal("10000"), 50);
        Order order = makeOrder(new BigDecimal("1"));
        List<String> violations = validator.validate(order, highLevAccount, config);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("exact min lot is accepted")
    void exactMinLot_valid() {
        Order order = makeOrder(new BigDecimal("0.01"));
        List<String> violations = validator.validate(order, account, config);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("exact max lot is accepted")
    void exactMaxLot_valid() {
        Order order = makeOrder(new BigDecimal("100"));
        List<String> violations = validator.validate(order, account, config);
        assertTrue(violations.isEmpty());
    }
}