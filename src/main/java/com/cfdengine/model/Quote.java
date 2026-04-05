package com.cfdengine.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Quote {

    private final BigDecimal bid;
    private final BigDecimal ask;
    private final Instant timestamp;

    public Quote(BigDecimal bid, BigDecimal ask, Instant timestamp){

        this.bid = bid;
        this.ask = ask;
        this.timestamp = timestamp;
    }

    public BigDecimal getBid(){
        return bid;
    }
    public BigDecimal getAsk(){
        return ask;
    }
    public Instant getTimestamp(){
        return timestamp;
    }
    public BigDecimal getSpread(){
        return ask.subtract(bid);
    }
}
