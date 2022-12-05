package com.emeraldhieu.vinci.order.logic;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderResponse}.
 */
@Mapper(componentModel = "spring")
public interface OrderResponseMapper extends EntityMapper<OrderResponse, Order> {

}