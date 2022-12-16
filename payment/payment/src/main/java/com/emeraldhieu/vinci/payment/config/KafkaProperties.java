package com.emeraldhieu.vinci.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.kafka")
@Data
public class KafkaProperties {
    private String bootstrapAddress;
    private String topic;
    private int partitions;
    private int replicationFactor;
    private String groupId;
}