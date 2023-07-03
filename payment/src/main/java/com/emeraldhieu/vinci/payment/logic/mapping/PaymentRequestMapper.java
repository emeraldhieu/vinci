package com.emeraldhieu.vinci.payment.logic.mapping;

import com.emeraldhieu.vinci.payment.logic.Payment;
import com.emeraldhieu.vinci.payment.logic.PaymentRequest;
import com.emeraldhieu.vinci.payment.utility.IgnoreUnmappedMapperConfig;
import com.emeraldhieu.vinci.payment.utility.RequestMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentRequest}.
 */
@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface PaymentRequestMapper extends RequestMapper<PaymentRequest, Payment> {

}