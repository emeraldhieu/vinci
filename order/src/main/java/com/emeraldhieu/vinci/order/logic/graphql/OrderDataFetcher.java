package com.emeraldhieu.vinci.order.logic.graphql;

import com.emeraldhieu.vinci.order.logic.OrderService;
import com.emeraldhieu.vinci.order.vinci.graphql.types.Order;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class OrderDataFetcher {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @DgsQuery
    public Order getOrder(@InputArgument String id) {
        var orderResponse = orderService.get(id);
        return orderMapper.map(orderResponse);
    }
}
