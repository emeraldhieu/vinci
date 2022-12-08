package com.emeraldhieu.vinci.payment.logic;

import com.emeraldhieu.vinci.payment.exception.NotFoundException;
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
    public PaymentResponse update(Long id, PaymentRequest paymentRequest) {
        Payment paymentToUpdate = paymentRepository
            .findById(id)
            .map(currentPayment -> {
                paymentRequestMapper.partialUpdate(currentPayment, paymentRequest);
                return currentPayment;
            })
            .orElseThrow(() -> new NotFoundException("Payment %s not found".formatted(id)));
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
    public PaymentResponse get(Long id) {
        return paymentRepository.findById(id)
            .map(paymentResponseMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Payment %s not found".formatted(id)));
    }

    @Override
    public void delete(Long id) {
        paymentRepository.findById(id)
            .ifPresent(payment -> paymentRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payments for query {}", query);
        //return paymentRepository.search(query, pageable).map(paymentMapper::toDto);
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
