package com.emeraldhieu.vinci.order.logic;

import com.emeraldhieu.vinci.order.OrderMessage;
import com.emeraldhieu.vinci.order.config.KafkaProperties;
import com.emeraldhieu.vinci.order.logic.event.OrderCreatedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Assert logs using JUnit 5's {@link OutputCaptureExtension}
 * See https://stackoverflow.com/questions/1827677/how-to-do-a-junit-assert-on-a-message-in-a-logger#60527144
 */
@ExtendWith(OutputCaptureExtension.class)
class OrderEventListenerTest {

    private KafkaProperties kafkaProperties;
    private KafkaTemplate kafkaTemplate;
    private OrderEventListener orderEventListener;

    @BeforeEach
    public void setUp() {
        kafkaProperties = mock(KafkaProperties.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        orderEventListener = new OrderEventListener(kafkaProperties, kafkaTemplate);
    }

    @Test
    public void givenOrderCreatedEvent_whenHandleOrderCreated_thenSendKafkaMessageAndLogSuccess(CapturedOutput output) {
        // GIVEN
        String topic = "amazingTopic";
        when(kafkaProperties.getTopic()).thenReturn(topic);

        String externalId = "awesomeId";
        OrderMessage orderMessage = OrderMessage.newBuilder()
            .setOrderId(externalId)
            .build();

        ProducerRecord<String, OrderMessage> producerRecord = mock(ProducerRecord.class);
        when(producerRecord.value()).thenReturn(orderMessage);

        long offset = 42;
        RecordMetadata recordMetadata = mock(RecordMetadata.class);
        when(recordMetadata.offset()).thenReturn(offset);

        CompletableFuture<SendResult<String, OrderMessage>> future = CompletableFuture.supplyAsync(() -> {
            SendResult sendResult = mock(SendResult.class);
            when(sendResult.getProducerRecord()).thenReturn(producerRecord);
            when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
            return sendResult;
        });
        when(kafkaTemplate.send(topic, orderMessage)).thenReturn(future);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(externalId);

        String logMessage = "Sent message=" + producerRecord.value() + " with offset=[" + recordMetadata.offset() + "]";

        // WHEN
        orderEventListener.handleOrderCreated(orderCreatedEvent);

        // THEN
        verify(kafkaTemplate, times(1)).send(topic, orderMessage);

        // Assert log printed
        assertThat(output.toString(), containsString(logMessage));
    }

    @Test
    @Disabled("Investigate why CapturedOutput doesn't work with multiple tests")
    public void givenOrderCreatedEvent_whenHandleOrderCreatedButFail_thenSendKafkaMessageAndLogFailure(CapturedOutput output) {
        // GIVEN
        String topic = "amazingTopic";
        when(kafkaProperties.getTopic()).thenReturn(topic);

        String externalId = "awesomeId";
        OrderMessage orderMessage = OrderMessage.newBuilder()
            .setOrderId(externalId)
            .build();

        ProducerRecord<String, OrderMessage> producerRecord = mock(ProducerRecord.class);
        when(producerRecord.value()).thenReturn(orderMessage);

        long offset = 42;
        RecordMetadata recordMetadata = mock(RecordMetadata.class);
        when(recordMetadata.offset()).thenReturn(offset);

        String errorMessage = "Something wrong";
        CompletableFuture<SendResult<String, OrderMessage>> future = CompletableFuture.supplyAsync(() -> {
            /*
            SendResult sendResult = mock(SendResult.class);
            when(sendResult.getProducerRecord()).thenReturn(producerRecord);
            when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
            return sendResult;
             */
            throw new IllegalArgumentException(errorMessage);
        });

        when(kafkaTemplate.send(topic, orderMessage)).thenReturn(future);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(externalId);

        String logMessage = "Unable to send message=" + producerRecord.value() + " due to : " + errorMessage;

        // WHEN
        orderEventListener.handleOrderCreated(orderCreatedEvent);

        // THEN
        verify(kafkaTemplate, times(1)).send(topic, orderMessage);

        // Assert log printed. TODO Investigate why the output doesn't record the log?
        //assertThat(output.toString(), containsString(logMessage));
    }
}