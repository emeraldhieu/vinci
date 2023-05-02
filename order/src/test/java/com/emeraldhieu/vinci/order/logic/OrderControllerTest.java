package com.emeraldhieu.vinci.order.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    private OrderService orderService;
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        orderService = mock(OrderService.class);
        orderController = new OrderController(orderService);
    }

    @Test
    void givenOrderRequest_whenCreate_thenReturnOrderResponse() {
        // GIVEN
        OrderRequest orderRequest = OrderRequest.builder()
            .build();
        String id = "awesomeId";
        OrderResponse orderResponse = OrderResponse.builder()
            .id(id)
            .build();
        when(orderService.create(orderRequest)).thenReturn(orderResponse);

        // WHEN
        ResponseEntity<OrderResponse> orderResponseEntity = orderController.createOrder(orderRequest);

        // THEN
        assertEquals(HttpStatus.CREATED, orderResponseEntity.getStatusCode());
        String uri = String.format(OrderController.ORDER_PATTERN, id);
        assertEquals(uri, orderResponseEntity.getHeaders().getLocation().toString());
    }

    @Test
    void deleteOrder() {
        // TODO Implement later
    }

    @Test
    void getOrder() {
        // TODO Implement later
    }

    @Test
    void listOrders() {
        // TODO Implement later
    }

    @Test
    void updateOrder() {
        // TODO Implement later
    }
}