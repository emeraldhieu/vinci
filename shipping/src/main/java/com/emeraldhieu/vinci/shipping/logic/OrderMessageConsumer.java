package com.emeraldhieu.vinci.shipping.logic;

import com.emeraldhieu.vinci.order.OrderMessage;
import com.emeraldhieu.vinci.shipping.config.KafkaProperties;
import com.emeraldhieu.vinci.shipping.grpc.ShippingRequest;
import com.emeraldhieu.vinci.shipping.grpc.ShippingResponse;
import com.emeraldhieu.vinci.shipping.grpc.StatusEnum;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Consume messages from order service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderMessageConsumer {

    private final KafkaProperties kafkaProperties;
    private final ShippingService shippingService;

    @KafkaListener(topics = "#{kafkaConfiguration.topic}", groupId = "#{kafkaConfiguration.groupId}")
    public void consumeMessage(OrderMessage order) {
        log.info(String.format("Received message in group %s: %s", kafkaProperties.getGroupId(), order));

        ShippingRequest shippingRequest = ShippingRequest.newBuilder()
            .setOrderId(order.getOrderId().toString())
            .setAmount(0)
            .setStatus(StatusEnum.DONE)
            .setShippingDate(Timestamp.newBuilder()
                .setSeconds(LocalDateTime.now().getSecond())
                .build())
            .build();

        shippingService.saveShipping(shippingRequest, new StreamObserver<>() {
            @Override
            public void onNext(ShippingResponse shippingResponse) {
                log.info("Shipping saved. What's next?");
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                log.info("Saving shipping is completed");
            }
        });
    }
}