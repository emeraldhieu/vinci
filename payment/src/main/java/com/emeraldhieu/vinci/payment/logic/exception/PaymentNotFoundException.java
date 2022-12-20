package com.emeraldhieu.vinci.payment.logic.exception;

import lombok.Getter;

@Getter
public class PaymentNotFoundException extends RuntimeException {

    private final String paymentId;

    public PaymentNotFoundException(String paymentId) {
        super();
        this.paymentId = paymentId;
    }
}
