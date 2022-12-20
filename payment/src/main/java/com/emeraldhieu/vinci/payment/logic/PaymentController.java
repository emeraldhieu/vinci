package com.emeraldhieu.vinci.payment.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentsApi {

    private final PaymentService paymentService;

    @Override
    public ResponseEntity<PaymentResponse> createPayment(PaymentRequest paymentRequest) {
        PaymentResponse createdPayment = paymentService.save(paymentRequest);
        return ResponseEntity.created(URI.create(String.format("/payments/%s", createdPayment.getId())))
            .body(createdPayment);
    }

    @Override
    public ResponseEntity<Void> deletePayment(String id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PaymentResponse> getPayment(String id) {
        PaymentResponse retrievedPayment = paymentService.get(id);
        return ResponseEntity.ok(retrievedPayment);
    }

    @Override
    public ResponseEntity<List<PaymentResponse>> listPayments(Integer offset, Integer limit, List<String> sortPayments) {
        Page<PaymentResponse> paymentResponsePage = paymentService.list(offset, limit, sortPayments);
        List<PaymentResponse> paymentResponses = paymentResponsePage.stream()
            .collect(Collectors.toList());
        return ResponseEntity.ok(paymentResponses);
    }

    @Override
    public ResponseEntity<PaymentResponse> updatePayment(String id, PaymentRequest paymentRequest) {
        PaymentResponse updatedPayment = paymentService.update(id, paymentRequest);
        return ResponseEntity.ok(updatedPayment);
    }
}
