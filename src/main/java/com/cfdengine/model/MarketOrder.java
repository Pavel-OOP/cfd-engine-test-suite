package com.cfdengine.model;

import com.cfdengine.model.enums.Direction;

import java.math.BigDecimal;

public class MarketOrder extends Order {
    public MarketOrder(Direction direction, BigDecimal size, String instrument) {
        super(direction, instrument, size);
    }
}
