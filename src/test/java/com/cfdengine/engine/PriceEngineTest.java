package com.cfdengine.engine;

import com.cfdengine.model.Quote;
import com.cfdengine.model.RawPrice;
import com.cfdengine.config.InstrumentConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class PriceEngineTest {

    private PriceEngine engine;
    private Instant now;

    @BeforeEach
    void setUp() {
        engine = new PriceEngine();
        now = Instant.now();
    }

    private InstrumentConfig eurusdConfig(BigDecimal spread) {
        return new InstrumentConfig(
                "EURUSD", spread, 100000,
                new BigDecimal("0.01"), new BigDecimal("100"),
                new BigDecimal("0.0001"), 30
        );
    }

    @Test
    @DisplayName("fixed spread: bid and ask equidistant from mid")
    void fixedSpread_equidistant() {
        RawPrice raw = new RawPrice(new BigDecimal("1.10500"), now);
        InstrumentConfig config = eurusdConfig(new BigDecimal("0.00020"));

        Quote quote = engine.applySpread(raw, config);

        assertEquals(new BigDecimal("1.10490"), quote.getBid());
        assertEquals(new BigDecimal("1.10510"), quote.getAsk());

        // Both sides should be exactly half-spread away from mid
        BigDecimal bidDistance = raw.getMid().subtract(quote.getBid());
        BigDecimal askDistance = quote.getAsk().subtract(raw.getMid());
        assertEquals(bidDistance, askDistance);
    }

    @Test
    @DisplayName("zero spread: bid equals ask equals mid")
    void zeroSpread_allEqual() {
        RawPrice raw = new RawPrice(new BigDecimal("1.10500"), now);
        InstrumentConfig config = eurusdConfig(BigDecimal.ZERO);

        Quote quote = engine.applySpread(raw, config);

        assertEquals(quote.getBid(), quote.getAsk());
        assertEquals(0, quote.getBid().compareTo(raw.getMid()));
    }

    @Test
    @DisplayName("JPY pair precision with 3 decimal places")
    void jpyPair_precision() {
        RawPrice raw = new RawPrice(new BigDecimal("145.500"), now);
        InstrumentConfig config = new InstrumentConfig(
                "USDJPY",
                new BigDecimal("0.020"),    // 2 pip spread for JPY
                100000,
                new BigDecimal("0.01"), new BigDecimal("100"),
                new BigDecimal("0.01"),     // JPY pip = 0.01
                30
        );

        Quote quote = engine.applySpread(raw, config);

        assertEquals(new BigDecimal("145.490"), quote.getBid());
        assertEquals(new BigDecimal("145.510"), quote.getAsk());
    }

    @Test
    @DisplayName("quote spread matches config spread")
    void spreadMatchesConfig() {
        BigDecimal configSpread = new BigDecimal("0.00020");
        RawPrice raw = new RawPrice(new BigDecimal("1.10500"), now);
        InstrumentConfig config = eurusdConfig(configSpread);

        Quote quote = engine.applySpread(raw, config);

        // quote.getSpread() = ask - bid should equal the config spread
        assertEquals(0, configSpread.compareTo(quote.getSpread()));
    }
}