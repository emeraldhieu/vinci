package com.emeraldhieu.vinci.order.logic.mapping;

import com.emeraldhieu.vinci.order.logic.Order;
import com.emeraldhieu.vinci.order.logic.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderResponse}.
 */
@Mapper(componentModel = "spring")
public interface OrderResponseMapper extends ResponseMapper<OrderResponse, Order> {

    @Mappings({
        @Mapping(source = "externalId", target = "id")
    })
    @Override
    OrderResponse toDto(Order entity);
}