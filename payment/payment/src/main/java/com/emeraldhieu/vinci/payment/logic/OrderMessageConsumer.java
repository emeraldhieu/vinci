package com.emeraldhieu.vinci.payment.logic;

import com.emeraldhieu.vinci.payment.OrderMessage;
import com.emeraldhieu.vinci.payment.config.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consume messages from order service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderMessageConsumer {

    private final KafkaProperties kafkaProperties;
    private final PaymentService paymentService;

    @KafkaListener(topics = "#{kafkaConfiguration.topic}", groupId = "#{kafkaConfiguration.groupId}")
    public void consumeMessage(OrderMessage order) {
        log.info(String.format("Received message in group %s: %s", kafkaProperties.getGroupId(), order));

        PaymentRequest paymentRequest = PaymentRequest.builder()
            .orderId(order.getOrderId().toString())
            .build();
        paymentService.save(paymentRequest);
    }
}