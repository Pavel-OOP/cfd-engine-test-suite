package com.cfdengine.model.enums;

public enum OrderStatus {
    PENDING,    // limit/stop waiting to trigger
    FILLED,     // executed
    CANCELLED,  // client cancelled
    REJECTED    // failed validation
}
