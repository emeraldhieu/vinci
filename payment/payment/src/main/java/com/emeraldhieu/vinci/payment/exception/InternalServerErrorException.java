package com.emeraldhieu.vinci.payment.exception;

public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }
}
