package com.emeraldhieu.vinci.order.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderResponse save(OrderRequest orderRequest);

    OrderResponse update(Long id, OrderRequest orderRequest);

    Page<OrderResponse> list(int offset, int limit, List<String> sortOrders);

    OrderResponse get(Long id);

    void delete(Long id);

    Page<OrderResponse> search(String query, Pageable pageable);
}