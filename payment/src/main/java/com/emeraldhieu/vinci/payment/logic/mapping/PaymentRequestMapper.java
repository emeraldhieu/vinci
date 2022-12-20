package com.emeraldhieu.vinci.payment.logic.mapping;

import com.emeraldhieu.vinci.payment.logic.Payment;
import com.emeraldhieu.vinci.payment.logic.PaymentRequest;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentRequest}.
 */
@Mapper(componentModel = "spring")
public interface PaymentRequestMapper extends RequestMapper<PaymentRequest, Payment> {

}