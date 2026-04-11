package com.cfdengine.engine;

import java.math.BigDecimal;

public class MarginCalculator {

    public BigDecimal requiredMargin(
            BigDecimal lotSize, int contractSize,
            BigDecimal price, int leverage) {
        return (lotSize.multiply(BigDecimal.valueOf(contractSize)).multiply(price)).divide(BigDecimal.valueOf(leverage));

    }

    public BigDecimal freeMargin(
            BigDecimal equity, BigDecimal usedMargin) {
        return equity.subtract(usedMargin);
    }

    public BigDecimal marginLevel(
            BigDecimal equity, BigDecimal usedMargin) {
        return equity.divide(usedMargin).multiply(BigDecimal.valueOf(100));
    }
}
