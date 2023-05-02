package com.emeraldhieu.vinci.order.logic.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderNotFoundExceptionTest {

    private OrderNotFoundException exception;

    @Test
    public void givenOrderId_whenCreateOrderNotFounException_thenReturnAnExceptionWithOrderId() {
        // GIVEN
        String orderId = "order42";

        // WHEN
        exception = new OrderNotFoundException(orderId);

        // THEN
        assertEquals(orderId, exception.getOrderId());
    }
}