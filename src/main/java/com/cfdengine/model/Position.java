package com.cfdengine.model;

import com.cfdengine.model.enums.Direction;
import com.cfdengine.model.enums.PositionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public class Position {

    private final Direction direction;
    private final BigDecimal size;
    private final String instrument;
    private final BigDecimal entryPrice;
    private final int contractSize;
    private final BigDecimal requiredMargin;
    private BigDecimal accumulatedSwap;
    private PositionStatus status;
    private final Instant openedAt;

    public Position(Direction direction, BigDecimal size, String instrument,
                    BigDecimal entryPrice, int contractSize, BigDecimal requiredMargin) {
        this.direction = direction;
        this.size = size;
        this.instrument = instrument;
        this.entryPrice = entryPrice;
        this.contractSize = contractSize;
        this.requiredMargin = requiredMargin;
        this.accumulatedSwap = BigDecimal.ZERO;
        this.status = PositionStatus.OPEN;
        this.openedAt = Instant.now();
    }

    public boolean isLong() { return direction == Direction.BUY; }

    public Direction getDirection() { return direction; }
    public BigDecimal getSize() { return size; }
    public String getInstrument() { return instrument; }
    public BigDecimal getEntryPrice() { return entryPrice; }
    public int getContractSize() { return contractSize; }
    public BigDecimal getRequiredMargin() { return requiredMargin; }
    public BigDecimal getAccumulatedSwap() { return accumulatedSwap; }
    public PositionStatus getStatus() { return status; }
    public Instant getOpenedAt() { return openedAt; }

    public void setStatus(PositionStatus status) { this.status = status; }
    public void addSwap(BigDecimal swap) {
        this.accumulatedSwap = this.accumulatedSwap.add(swap);
    }
}