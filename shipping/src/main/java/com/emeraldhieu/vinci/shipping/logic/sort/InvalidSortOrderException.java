package com.emeraldhieu.vinci.shipping.logic.sort;

public class InvalidSortOrderException extends RuntimeException {

    public InvalidSortOrderException(String message) {
        super(message);
    }

    public InvalidSortOrderException(Throwable cause) {
        super(cause);
    }

    public InvalidSortOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
