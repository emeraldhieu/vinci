package com.emeraldhieu.vinci.shipping.logic.mapping;

import com.emeraldhieu.vinci.shipping.grpc.ShippingResponse;
import com.emeraldhieu.vinci.shipping.grpc.StatusEnum;
import com.emeraldhieu.vinci.shipping.logic.Shipping;
import com.emeraldhieu.vinci.shipping.logic.Status;
import com.emeraldhieu.vinci.shipping.utility.IgnoreUnmappedMapperConfig;
import com.emeraldhieu.vinci.shipping.utility.ResponseMapper;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

import java.time.LocalDateTime;

/**
 * Mapper for the entity {@link com.emeraldhieu.vinci.shipping.logic.Shipping} and its DTO {@link com.emeraldhieu.vinci.shipping.grpc.ShippingResponse}.
 */
@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface ShippingResponseMapper extends ResponseMapper<ShippingResponse, Shipping> {

    @Mappings({
        @Mapping(source = "externalId", target = "id")
    })
    @Override
    ShippingResponse toDto(Shipping entity);

    @Named("externalIdToId")
    static String externalIdToId(String externalId) {
        return externalId;
    }

    @ValueMappings({
        @ValueMapping(target = "UNRECOGNIZED", source = "UNRECOGNIZED"),
        @ValueMapping(target = "IN_PROGRESS", source = "IN_PROGRESS"),
        @ValueMapping(target = "DONE", source = "DONE"),
        @ValueMapping(target = "PENDING", source = "PENDING")
    })
    StatusEnum statusToStatusEnum(Status status);

    @Mappings({
        @Mapping(source = "shippingDate", target = "shippingDate")
    })
    default Timestamp LocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.newBuilder()
            .setSeconds(localDateTime.getSecond())
            .build();
        return timestamp;
    }
}