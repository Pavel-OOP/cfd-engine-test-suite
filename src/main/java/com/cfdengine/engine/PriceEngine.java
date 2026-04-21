package com.cfdengine.engine;

import com.cfdengine.model.Quote;
import com.cfdengine.model.RawPrice;
import com.cfdengine.config.InstrumentConfig;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceEngine {

    public Quote applySpread(RawPrice raw, InstrumentConfig config) {
        BigDecimal halfSpread = config.getSpread()
                .divide(BigDecimal.valueOf(2), 5, RoundingMode.HALF_UP);

        BigDecimal bid = raw.getMid().subtract(halfSpread);
        BigDecimal ask = raw.getMid().add(halfSpread);

        return new Quote(bid, ask, raw.getTimestamp());
    }
}