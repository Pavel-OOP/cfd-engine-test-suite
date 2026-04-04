package com.cfdengine.model;

import com.cfdengine.model.enums.Direction;

import java.math.BigDecimal;

public abstract class Order {
    private final Direction direction;
    private final String instrument;
    private final BigDecimal size;

    protected Order(Direction direction, String instrument, BigDecimal size) {
        this.direction = direction;
        this.instrument = instrument;
        this.size = size;
    }
}
