package com.emeraldhieu.vinci.shipping.logic.exception;

import lombok.Getter;

@Getter
public class ShippingNotFoundException extends RuntimeException {

    private final String shippingId;

    public ShippingNotFoundException(String shippingId) {
        super();
        this.shippingId = shippingId;
    }
}
