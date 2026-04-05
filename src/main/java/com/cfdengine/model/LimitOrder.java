package com.cfdengine.model;

import com.cfdengine.model.enums.Direction;

import java.math.BigDecimal;
import java.time.Instant;

public class LimitOrder extends Order {
    private final BigDecimal targetPrice;
    private final Instant expiry;

    public LimitOrder(Direction direction, BigDecimal size,
                      String instrument, BigDecimal targetPrice, Instant expiry) {
        super(direction, instrument, size);
        this.targetPrice = targetPrice;
        this.expiry = expiry;
    }
}
