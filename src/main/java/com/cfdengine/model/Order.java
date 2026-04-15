package com.cfdengine.model;

import com.cfdengine.model.enums.Direction;
import com.cfdengine.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public abstract class Order {
    private final Direction direction;
    private final String instrument;
    private final BigDecimal size;
    private final Instant createdAt;
    private OrderStatus status;


    protected Order(Direction direction, String instrument, BigDecimal size) {
        this.direction = direction;
        this.instrument = instrument;
        this.size = size;
        this.status = OrderStatus.PENDING;
        this.createdAt = Instant.now();
    }
    public BigDecimal getSize(){return size;}
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getInstrument() { return instrument; }
}
