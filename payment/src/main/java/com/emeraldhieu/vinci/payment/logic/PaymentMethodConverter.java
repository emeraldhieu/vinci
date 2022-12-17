package com.emeraldhieu.vinci.payment.logic;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convert enum to enum's property and vice versa.
 * See https://www.baeldung.com/jpa-attribute-converters.
 */
@Converter
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {

    @Override
    public String convertToDatabaseColumn(PaymentMethod paymentMethod) {
        return paymentMethod.getKeyword();
    }

    @Override
    public PaymentMethod convertToEntityAttribute(String keyword) {
        return PaymentMethod.forKeyword(keyword);
    }
}