package com.emeraldhieu.vinci.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperties {
    private String host;
    private String port;
}
