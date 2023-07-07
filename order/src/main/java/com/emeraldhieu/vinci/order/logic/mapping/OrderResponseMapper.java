package com.emeraldhieu.vinci.order.logic.mapping;

import com.emeraldhieu.vinci.order.logic.Order;
import com.emeraldhieu.vinci.order.logic.OrderResponse;
import com.emeraldhieu.vinci.order.utility.IgnoreUnmappedMapperConfig;
import com.emeraldhieu.vinci.order.utility.ResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderResponse}.
 */
@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface OrderResponseMapper extends ResponseMapper<OrderResponse, Order> {

    @Mappings({
        @Mapping(source = "externalId", target = "id")
    })
    @Override
    OrderResponse toDto(Order entity);
}