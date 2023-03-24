package com.emeraldhieu.vinci.shipping.logic.mapping;

import com.emeraldhieu.vinci.shipping.grpc.ShippingRequest;
import com.emeraldhieu.vinci.shipping.logic.Shipping;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Mapper for the entity {@link Shipping} and its DTO {@link ShippingRequest}.
 */
@Mapper(componentModel = "spring")
public interface ShippingRequestMapper extends RequestMapper<ShippingRequest, Shipping> {

    @Mappings({
        @Mapping(source = "id", target = "externalId",
            /**
             * Because Protobuf doesn't allow null,
             * "unset" properties contain empty string "" instead of null.
            */
            conditionExpression = "java(!request.getId().isEmpty())"),
        @Mapping(target = "id", ignore = true)
    })
    @Override
    Shipping toEntity(ShippingRequest request);

    @Mappings({
        @Mapping(source = "shippingDate", target = "shippingDate")
    })
    default LocalDateTime LocalDateTimeToTimestamp(Timestamp shippingDate) {
        return Instant
            .ofEpochSecond(shippingDate.getSeconds(), shippingDate.getNanos())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
}