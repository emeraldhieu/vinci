package com.emeraldhieu.vinci.order.config;

import com.emeraldhieu.vinci.payment.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfiguration {

    private final KafkaProperties kafkaProperties;

    /**
     * Try customizing autoconfigured {@link DefaultKafkaProducerFactory},
     * meaning configuring producer using Java code.
     */
    @Bean
    public DefaultKafkaProducerFactoryCustomizer defaultKafkaProducerFactoryCustomizer() {
        return producerFactory -> {
            Map<String, Object> additionalConfigs = Map.of(
                ProducerConfig.RETRIES_CONFIG, 5
            );
            producerFactory.updateConfigs(additionalConfigs);
        };
    }

    @Bean
    public KafkaTemplate<String, OrderMessage> kafkaTemplate(ProducerFactory producerFactory) {
        KafkaTemplate<String, OrderMessage> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setProducerListener(new ProducerListener<>() {
            @Override
            public void onSuccess(ProducerRecord<String, OrderMessage> producerRecord, RecordMetadata recordMetadata) {
                log.info("Sent a record");
            }

            @Override
            public void onError(ProducerRecord<String, OrderMessage> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                log.info("Fail to send a record");
            }
        });
        return kafkaTemplate;
    }

    /**
     * Create topic "orders" if not existed.
     */
    @Bean
    public NewTopic orderTopic() {
        return new NewTopic(kafkaProperties.getTopic(),
            kafkaProperties.getPartitions(),
            (short) kafkaProperties.getReplicationFactor());
    }
}