package com.emeraldhieu.vinci.order.logic.graphql;

import com.emeraldhieu.vinci.order.logic.OrderResponse;
import com.emeraldhieu.vinci.order.utility.IgnoreUnmappedMapperConfig;
import com.emeraldhieu.vinci.order.vinci.graphql.types.Order;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper that maps into {@link Order}.
 */
@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
interface OrderMapper {

    Order map(OrderResponse orderResponse);

    List<Order> map(List<OrderResponse> orderResponses);
}
