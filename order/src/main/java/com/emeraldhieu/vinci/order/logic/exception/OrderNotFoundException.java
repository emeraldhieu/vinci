package com.emeraldhieu.vinci.order.logic.exception;

import lombok.Getter;

@Getter
public class OrderNotFoundException extends RuntimeException {

    private final String orderId;

    public OrderNotFoundException(String orderId) {
        super();
        this.orderId = orderId;
    }
}
