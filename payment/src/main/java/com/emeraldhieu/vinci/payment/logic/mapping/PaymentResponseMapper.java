package com.emeraldhieu.vinci.payment.logic.mapping;

import com.emeraldhieu.vinci.payment.logic.Payment;
import com.emeraldhieu.vinci.payment.logic.PaymentMethod;
import com.emeraldhieu.vinci.payment.logic.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentResponse}.
 */
@Mapper(componentModel = "spring")
public interface PaymentResponseMapper extends ResponseMapper<PaymentResponse, Payment> {

    @ValueMappings({
        @ValueMapping(target = "DEBIT", source = "DEBIT"),
        @ValueMapping(target = "CREDIT", source = "CREDIT")
    })
    PaymentResponse.PaymentMethodEnum paymentMethodToPaymentMethodEnum(PaymentMethod paymentMethod);

    @Mappings({
        @Mapping(source = "externalId", target = "id")
    })
    @Override
    PaymentResponse toDto(Payment entity);
}