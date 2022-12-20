package com.emeraldhieu.vinci.payment.logic;

import com.emeraldhieu.vinci.payment.logic.exception.PaymentNotFoundException;
import com.emeraldhieu.vinci.payment.logic.mapping.PaymentRequestMapper;
import com.emeraldhieu.vinci.payment.logic.mapping.PaymentResponseMapper;
import com.emeraldhieu.vinci.payment.logic.sort.SortOrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultPaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentRequestMapper paymentRequestMapper;
    private final PaymentResponseMapper paymentResponseMapper;
    private final SortOrderValidator sortOrderValidator;

    @Override
    public PaymentResponse save(PaymentRequest paymentRequest) {
        Payment paymentToSave = paymentRequestMapper.toEntity(paymentRequest);
        Payment savedPayment = paymentRepository.save(paymentToSave);
        return paymentResponseMapper.toDto(savedPayment);
    }

    @Override
    public PaymentResponse update(String id, PaymentRequest paymentRequest) {
        Payment paymentToUpdate = paymentRepository
            .findByExternalId(id)
            .map(currentPayment -> {
                paymentRequestMapper.partialUpdate(currentPayment, paymentRequest);
                return currentPayment;
            })
            .orElseThrow(() -> new PaymentNotFoundException((id)));
        Payment updatedPayment = paymentRepository.save(paymentToUpdate);
        return paymentResponseMapper.toDto(updatedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> list(int offset, int limit, List<String> sortPayments) {
        sortOrderValidator.validate(sortPayments);

        List<Sort.Order> theSortPayments = sortPayments.stream()
            .map(sortPayment -> {
                String[] tokens = sortPayment.split(",");
                String propertyName = tokens[0];
                String direction = tokens[1];
                return Sort.Order.by(propertyName)
                    .with(Sort.Direction.fromString(direction));
            })
            .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(theSortPayments));
        return paymentRepository.findAll(pageable)
            .map(paymentResponseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse get(String id) {
        return paymentRepository.findByExternalId(id)
            .map(paymentResponseMapper::toDto)
            .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public void delete(String id) {
        paymentRepository.findByExternalId(id)
            .ifPresent(payment -> paymentRepository.deleteByExternalId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payments for query {}", query);
        //return paymentRepository.search(query, pageable).map(paymentMapper::toDto);
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
