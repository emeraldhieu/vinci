package com.emeraldhieu.vinci.payment.logic;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentResponse}.
 */
@Mapper(componentModel = "spring")
public interface PaymentResponseMapper extends EntityMapper<PaymentResponse, Payment> {

}