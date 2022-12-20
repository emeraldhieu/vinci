package com.emeraldhieu.vinci.order.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        OrderResponse createdOrder = orderService.create(orderRequest);
        return ResponseEntity.created(URI.create(String.format("/orders/%s", createdOrder.getId())))
            .body(createdOrder);
    }

    @Override
    public ResponseEntity<Void> deleteOrder(String id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<OrderResponse> getOrder(String id) {
        OrderResponse retrievedOrder = orderService.get(id);
        return ResponseEntity.ok(retrievedOrder);
    }

    @Override
    public ResponseEntity<List<OrderResponse>> listOrders(Integer offset, Integer limit, List<String> sortOrders) {
        Page<OrderResponse> orderResponsePage = orderService.list(offset, limit, sortOrders);
        List<OrderResponse> orderResponses = orderResponsePage.stream()
            .collect(Collectors.toList());
        return ResponseEntity.ok(orderResponses);
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrder(String id, OrderRequest orderRequest) {
        OrderResponse updatedOrder = orderService.update(id, orderRequest);
        return ResponseEntity.ok(updatedOrder);
    }
}
