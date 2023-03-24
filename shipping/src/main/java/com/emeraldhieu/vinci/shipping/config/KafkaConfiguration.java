package com.emeraldhieu.vinci.shipping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    public String getTopic() {
        return kafkaProperties.getTopic();
    }

    public String getGroupId() {
        return kafkaProperties.getGroupId();
    }
}
