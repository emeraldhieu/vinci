package com.emeraldhieu.vinci.payment.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

    @JsonProperty("debit")
    DEBIT("debit", "Debit"),

    @JsonProperty("credit")
    CREDIT("credit", "Credit");

    private final String keyword;
    private final String name;
    private static final Map<String, PaymentMethod> paymentMethodsByKeyword = new HashMap<>();

    static {
        Arrays.stream(PaymentMethod.values()).forEach(unit -> {
            paymentMethodsByKeyword.put(unit.getKeyword(), unit);
        });
    }

    public static PaymentMethod forKeyword(String keyword) {
        return Optional.ofNullable(paymentMethodsByKeyword.get(keyword))
            .orElseThrow(() -> new IllegalArgumentException(String.format("Payment method '%s' not found.", keyword)));
    }

    public String toKeyword() {
        return keyword;
    }

}
