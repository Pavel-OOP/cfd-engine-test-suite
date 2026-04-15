package com.cfdengine.engine;

import com.cfdengine.model.Order;
import com.cfdengine.model.Account;
import com.cfdengine.config.InstrumentConfig;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderValidator {

    public List<String> validate(Order order, Account account, InstrumentConfig config) {
        List<String> violations = new ArrayList<>();

        // 1. Size must be positive
        if (order.getSize().compareTo(BigDecimal.ZERO) <= 0) {
            violations.add("Order size must be greater than zero");
        }

        // 2. Size must meet minimum lot
        if (order.getSize().compareTo(config.getMinLot()) < 0) {
            violations.add("Order size below minimum lot: " + config.getMinLot());
        }

        // 3. Size must not exceed maximum lot
        if (order.getSize().compareTo(config.getMaxLot()) > 0) {
            violations.add("Order size exceeds maximum lot: " + config.getMaxLot());
        }

        // 4. Account leverage must not exceed instrument max
        if (account.getMaxLeverage() > config.getMaxLeverage()) {
            violations.add("Leverage " + account.getMaxLeverage()
                    + " exceeds instrument max: " + config.getMaxLeverage());
        }

        // 5. Instrument must be specified
        if (order.getInstrument() == null || order.getInstrument().isEmpty()) {
            violations.add("Instrument must not be null or empty");
        }

        return violations;
    }
}