package com.emeraldhieu.vinci.order.logic.mapping;

import com.emeraldhieu.vinci.order.logic.Order;
import com.emeraldhieu.vinci.order.logic.OrderRequest;
import com.emeraldhieu.vinci.order.utility.IgnoreUnmappedMapperConfig;
import com.emeraldhieu.vinci.order.utility.RequestMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderRequest}.
 */
@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface OrderRequestMapper extends RequestMapper<OrderRequest, Order> {

}