package com.emeraldhieu.vinci.order.logic;

import com.emeraldhieu.vinci.order.config.KafkaProperties;
import com.emeraldhieu.vinci.order.logic.event.OrderCreatedEvent;
import com.emeraldhieu.vinci.payment.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        OrderMessage orderMessage = OrderMessage.newBuilder()
            .setOrderId(event.externalId())
            .build();
        CompletableFuture<SendResult<String, OrderMessage>> future =
            kafkaTemplate.send(kafkaProperties.getTopic(), orderMessage);
        future.whenComplete((result, throwable) -> {
            log.info("Sent message=" + result.getProducerRecord().value() + " with offset=[" + result.getRecordMetadata().offset() + "]");
            if (throwable != null) {
                log.info("Unable to send message=" + result.getProducerRecord().value() + " due to : " + throwable.getMessage());
            }
        });
    }
}
