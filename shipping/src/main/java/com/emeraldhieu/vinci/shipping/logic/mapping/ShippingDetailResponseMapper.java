package com.emeraldhieu.vinci.shipping.logic.mapping;

import com.emeraldhieu.vinci.shipping.grpc.ShippingDetailResponse;
import com.emeraldhieu.vinci.shipping.logic.ShippingDetail;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDateTime;

/**
 * Mapper for the entity {@link ShippingDetail} and its DTO {@link ShippingDetailResponse}.
 */
@Mapper(componentModel = "spring")
public interface ShippingDetailResponseMapper extends ResponseMapper<ShippingDetailResponse, ShippingDetail> {

    @Mappings({
        @Mapping(source = "externalId", target = "id"),
        @Mapping(source = "shipping.externalId", target = "shippingId")
    })
    @Override
    ShippingDetailResponse toDto(ShippingDetail entity);

    @Named("externalIdToId")
    static String externalIdToId(String externalId) {
        return externalId;
    }

    @Mappings({
        @Mapping(source = "createdAt", target = "createdAt"),
        @Mapping(source = "updatedAt", target = "updatedAt")
    })
    default Timestamp LocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.newBuilder()
            .setSeconds(localDateTime.getSecond())
            .build();
        return timestamp;
    }
}