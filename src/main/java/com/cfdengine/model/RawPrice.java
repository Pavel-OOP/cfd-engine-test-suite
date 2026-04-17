package com.cfdengine.model;

import java.math.BigDecimal;
import java.time.Instant;

public class RawPrice {
    private final BigDecimal mid;
    private final Instant timestamp;

    public RawPrice(BigDecimal mid, Instant timestamp) {
        this.mid = mid;
        this.timestamp = timestamp;
    }

    public BigDecimal getMid() { return mid; }
    public Instant getTimestamp() { return timestamp; }
}