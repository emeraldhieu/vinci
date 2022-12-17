package com.emeraldhieu.vinci.payment.logic;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentRequest}.
 */
@Mapper(componentModel = "spring")
public interface PaymentRequestMapper extends EntityMapper<PaymentRequest, Payment> {

}