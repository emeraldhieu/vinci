package com.emeraldhieu.vinci.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Scan property classes annotated with {@link ConfigurationProperties}.
 * See https://www.baeldung.com/configuration-properties-in-spring-boot#1-spring-boot-22
 */
@ConfigurationPropertiesScan(basePackageClasses = {OrderApp.class})
@SpringBootApplication
public class OrderApp {

    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}