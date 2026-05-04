package com.cfdengine.engine;

import com.cfdengine.model.Position;
import com.cfdengine.model.Quote;
import java.math.BigDecimal;

public class PnLCalculator {

    public BigDecimal unrealisedPnL(Position pos, Quote currentQuote) {
        BigDecimal exitPrice = pos.isLong()
                ? currentQuote.getBid()
                : currentQuote.getAsk();

        BigDecimal priceDiff = pos.isLong()
                ? exitPrice.subtract(pos.getEntryPrice())
                : pos.getEntryPrice().subtract(exitPrice);

        return priceDiff
                .multiply(pos.getSize())
                .multiply(BigDecimal.valueOf(pos.getContractSize()));
    }
}
