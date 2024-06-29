package com.emeraldhieu.vinci.order.logic.config;

import com.emeraldhieu.vinci.order.config.KafkaProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * A properties class used for testing because it contains some properties not existed in {@link KafkaProperties}.
 * ---
 * {@link ConfigurationProperties} is scanned by {@link ConfigurationPropertiesScan}.
 */
@ConfigurationProperties(prefix = "application.kafka")
@Data
public class KafkaTestProperties {
    private String bootstrapAddress;
    private String topic;
    private int partitions;
    private int replicationFactor;
    private String groupId;
}