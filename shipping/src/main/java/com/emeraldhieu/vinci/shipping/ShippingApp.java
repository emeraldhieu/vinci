package com.emeraldhieu.vinci.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Scan property classes annotated with {@link ConfigurationProperties}.
 * See https://www.baeldung.com/configuration-properties-in-spring-boot#1-spring-boot-22
 */
@ConfigurationPropertiesScan(basePackageClasses = {ShippingApp.class})
@SpringBootApplication
public class ShippingApp {

    public static void main(String[] args) {
        SpringApplication.run(ShippingApp.class, args);
    }
}