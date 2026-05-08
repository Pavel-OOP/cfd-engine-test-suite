package com.cfdengine.engine;

import com.cfdengine.model.Position;
import com.cfdengine.model.Quote;
import com.cfdengine.model.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class PnLCalculatorTest {

    private PnLCalculator calculator;
    private Instant now;

    @BeforeEach
    void setUp() {
        calculator = new PnLCalculator();
        now = Instant.now();
    }

    private Position makePosition(Direction direction, String entryPrice) {
        return new Position(
                direction,
                new BigDecimal("1"),
                "EURUSD",
                new BigDecimal(entryPrice),
                100000,
                new BigDecimal("3683.33")
        );
    }

    private Quote makeQuote(String bid, String ask) {
        return new Quote(new BigDecimal(bid), new BigDecimal(ask), now);
    }

    @Test
    @DisplayName("long position, price goes up → positive PnL")
    void longPriceUp_positivePnL() {
        Position pos = makePosition(Direction.BUY, "1.10500");
        Quote current = makeQuote("1.10700", "1.10720");

        BigDecimal pnl = calculator.unrealisedPnL(pos, current);

        // (1.10700 - 1.10500) * 1 * 100000 = 200.00
        assertEquals(0, new BigDecimal("200").compareTo(pnl));
    }

    @Test
    @DisplayName("short position, price goes down → positive P&L")
    void shortPriceDown_positivePnL() {
        Position pos = makePosition(Direction.SELL, "1.10500");
        Quote current = makeQuote("1.10280", "1.10300");

        BigDecimal pnl = calculator.unrealisedPnL(pos, current);

        // (1.10500 - 1.10300) * 1 * 100000 = 200.00
        assertEquals(0, new BigDecimal("200").compareTo(pnl));
    }

    @Test
    @DisplayName("long position, price goes down → negative P&L")
    void longPriceDown_negativePnL() {
        Position pos = makePosition(Direction.BUY, "1.10500");
        Quote current = makeQuote("1.10200", "1.10220");

        BigDecimal pnl = calculator.unrealisedPnL(pos, current);

        // (1.10200 - 1.10500) * 1 * 100000 = -300.00
        assertEquals(0, new BigDecimal("-300").compareTo(pnl));
    }

    @Test
    @DisplayName("immediate P&L after open = negative spread cost")
    void immediateOpen_negativePnL() {
        Position pos = makePosition(Direction.BUY, "1.10520");
        Quote current = makeQuote("1.10500", "1.10520");

        BigDecimal pnl = calculator.unrealisedPnL(pos, current);

        // (1.10500 - 1.10520) * 1 * 100000 = -20.00 (the spread cost)
        assertEquals(0, new BigDecimal("-20").compareTo(pnl));
    }

    @Test
    @DisplayName("P&L crosses zero: was positive, now negative")
    void pnlCrossesZero() {
        Position pos = makePosition(Direction.BUY, "1.10500");

        Quote up = makeQuote("1.10700", "1.10720");
        BigDecimal pnlUp = calculator.unrealisedPnL(pos, up);
        assertTrue(pnlUp.compareTo(BigDecimal.ZERO) > 0,
                "P&L should be positive when price is up");

        Quote down = makeQuote("1.10100", "1.10120");
        BigDecimal pnlDown = calculator.unrealisedPnL(pos, down);
        assertTrue(pnlDown.compareTo(BigDecimal.ZERO) < 0,
                "P&L should be negative when price drops below entry");
    }
}