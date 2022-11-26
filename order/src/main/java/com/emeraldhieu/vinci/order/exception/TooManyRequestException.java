package com.emeraldhieu.vinci.order.exception;

public class TooManyRequestException extends RuntimeException {

    public TooManyRequestException(String message) {
        super(message);
    }

    public TooManyRequestException(Throwable cause) {
        super(cause);
    }
}
