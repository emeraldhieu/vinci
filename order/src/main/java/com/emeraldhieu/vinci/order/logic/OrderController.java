package com.emeraldhieu.vinci.order.logic;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<OrderResponse> getOrder(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<OrderResponse> listOrders() {
        return null;
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrder(Long id, OrderRequest orderRequest) {
        return null;
    }
}
