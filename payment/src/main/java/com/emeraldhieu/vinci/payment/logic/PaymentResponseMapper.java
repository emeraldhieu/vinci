package com.emeraldhieu.vinci.payment.logic;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentResponse}.
 */
@Mapper(componentModel = "spring")
public interface PaymentResponseMapper extends EntityMapper<PaymentResponse, Payment> {

    @ValueMappings({
        @ValueMapping(target = "DEBIT", source = "DEBIT"),
        @ValueMapping(target = "CREDIT", source = "CREDIT")
    })
    PaymentResponse.PaymentMethodEnum paymentMethodToPaymentMethodEnum(PaymentMethod paymentMethod);
}