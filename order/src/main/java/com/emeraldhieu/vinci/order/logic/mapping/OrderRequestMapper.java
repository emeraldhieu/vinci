package com.emeraldhieu.vinci.order.logic.mapping;

import com.emeraldhieu.vinci.order.logic.Order;
import com.emeraldhieu.vinci.order.logic.OrderRequest;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderRequest}.
 */
@Mapper(componentModel = "spring")
public interface OrderRequestMapper extends RequestMapper<OrderRequest, Order> {

}