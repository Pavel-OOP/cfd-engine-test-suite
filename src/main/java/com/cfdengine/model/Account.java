package com.cfdengine.model;

import java.math.BigDecimal;

public class Account {

    private BigDecimal balance;
    private BigDecimal usedMargin;
    private final int maxLeverage;

    public Account(BigDecimal balance, int maxLeverage) {
        this.balance = balance;
        this.maxLeverage = maxLeverage;
        this.usedMargin = BigDecimal.ZERO;
    }

    public BigDecimal getBalance() { return balance; }
    public BigDecimal getUsedMargin() { return usedMargin; }
    public int getMaxLeverage() { return maxLeverage; }

    public void addToBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void lockMargin(BigDecimal margin) {
        this.usedMargin = this.usedMargin.add(margin);
    }

    public void releaseMargin(BigDecimal margin) {
        this.usedMargin = this.usedMargin.subtract(margin);
    }
}