package com.emeraldhieu.vinci.order.logic;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderRequest}.
 */
@Mapper(componentModel = "spring")
public interface OrderRequestMapper extends EntityMapper<OrderRequest, Order> {

}