package com.emeraldhieu.vinci.payment.exception;

public class TooManyRequestException extends RuntimeException {

    public TooManyRequestException(String message) {
        super(message);
    }

    public TooManyRequestException(Throwable cause) {
        super(cause);
    }
}
