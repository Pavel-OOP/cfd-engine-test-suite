package com.cfdengine.model;

import java.math.BigDecimal;
import java.time.Instant;

public class FillResult {

    private final BigDecimal fillPrice;
    private final BigDecimal size;
    private final Instant filledAt;

    public FillResult(BigDecimal fillPrice, BigDecimal size, Instant filledAt) {
        this.fillPrice = fillPrice;
        this.size = size;
        this.filledAt = filledAt;
    }

    public BigDecimal getFillPrice() { return fillPrice; }
    public BigDecimal getSize() { return size; }
    public Instant getFilledAt() { return filledAt; }
}