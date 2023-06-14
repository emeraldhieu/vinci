package com.emeraldhieu.vinci.shipping.config;

import com.emeraldhieu.vinci.shipping.logic.ShippingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackageClasses = {ShippingRepository.class})
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableTransactionManagement
public class DatabaseConfiguration {

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
