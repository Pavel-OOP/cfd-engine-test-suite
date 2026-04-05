package com.cfdengine.model;

import com.cfdengine.model.enums.Direction;

import java.math.BigDecimal;

public class StopOrder extends Order {
    private final BigDecimal triggerPrice;
    private boolean triggered;

    public StopOrder(Direction direction, BigDecimal size,
                     String instrument, BigDecimal triggerPrice) {
        super(direction, instrument, size);
        this.triggerPrice = triggerPrice;
        this.triggered = false;
    }
}
