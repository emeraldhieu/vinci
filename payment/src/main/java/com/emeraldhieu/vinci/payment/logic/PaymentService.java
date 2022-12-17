package com.emeraldhieu.vinci.payment.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {

    PaymentResponse save(PaymentRequest paymentRequest);

    PaymentResponse update(Long id, PaymentRequest paymentRequest);

    Page<PaymentResponse> list(int offset, int limit, List<String> sortPayments);

    PaymentResponse get(Long id);

    void delete(Long id);

    Page<PaymentResponse> search(String query, Pageable pageable);
}