package com.emeraldhieu.vinci.order.logic.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * A class that customizes Kafka for testing.
 */
@TestConfiguration
@RequiredArgsConstructor
public class KafkaTestConfiguration {

    private final KafkaTestProperties kafkaTestProperties;

    @Bean
    DefaultKafkaConsumerFactoryCustomizer defaultKafkaConsumerFactoryCustomizer() {
        return consumerFactory -> {
            Map<String, Object> additionalConfigs = Map.of(
                // Set consumer's groupId which hasn't been set anywhere.
                ConsumerConfig.GROUP_ID_CONFIG, kafkaTestProperties.getGroupId()
            );
            consumerFactory.updateConfigs(additionalConfigs);
        };
    }
}