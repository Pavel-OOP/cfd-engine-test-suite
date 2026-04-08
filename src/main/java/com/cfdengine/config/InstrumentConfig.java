package com.cfdengine.config;

import java.math.BigDecimal;

public class InstrumentConfig {

    private final String symbol;
    private final BigDecimal spread;
    private final int contractSize;
    private final BigDecimal minLot;
    private final BigDecimal maxLot;
    private final BigDecimal pipSize;
    private final int maxLeverage;

    public InstrumentConfig(String symbol, BigDecimal spread,
                            int contractSize, BigDecimal minLot,
                            BigDecimal maxLot, BigDecimal pipSize,
                            int maxLeverage) {
        this.symbol = symbol;
        this.spread = spread;
        this.contractSize = contractSize;
        this.minLot = minLot;
        this.maxLot = maxLot;
        this.pipSize = pipSize;
        this.maxLeverage = maxLeverage;
    }

    public String getSymbol() { return symbol; }
    public BigDecimal getSpread() { return spread; }
    public int getContractSize() { return contractSize; }
    public BigDecimal getMinLot() { return minLot; }
    public BigDecimal getMaxLot() { return maxLot; }
    public BigDecimal getPipSize() { return pipSize; }
    public int getMaxLeverage() { return maxLeverage; }
}