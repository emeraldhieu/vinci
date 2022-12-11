package com.emeraldhieu.vinci.order.logic;

import com.emeraldhieu.vinci.order.config.KafkaProperties;
import com.emeraldhieu.vinci.order.logic.event.OrderCreatedEvent;
import com.emeraldhieu.vinci.order.logic.event.OrderDeletedEvent;
import com.emeraldhieu.vinci.order.logic.event.OrderUpdatedEvent;
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
    private final KafkaTemplate<String, String> kafkaTemplate;

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info(event.externalId());
        CompletableFuture<SendResult<String, String>> future =
            kafkaTemplate.send(kafkaProperties.getTopic(), event.externalId());
        future.whenComplete((result, throwable) -> {
            System.out.println("Sent message=[" + event.externalId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            if (throwable != null) {
                System.out.println("Unable to send message=[" + event.externalId() + "] due to : " + throwable.getMessage());
            }
        });
    }

    @EventListener
    public void handleOrderUpdated(OrderUpdatedEvent event) {
        log.info(event.externalId());
        CompletableFuture<SendResult<String, String>> future =
            kafkaTemplate.send(kafkaProperties.getTopic(), event.externalId());
    }

    @EventListener
    public void handleOrderDeleted(OrderDeletedEvent event) {
        log.info(event.externalId());
        CompletableFuture<SendResult<String, String>> future =
            kafkaTemplate.send(kafkaProperties.getTopic(), event.externalId());
    }
}
