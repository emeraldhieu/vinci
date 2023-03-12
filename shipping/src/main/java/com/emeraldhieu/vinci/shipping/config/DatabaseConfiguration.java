package com.emeraldhieu.vinci.shipping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"com.emeraldhieu.vinci.shipping.logic"})
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfiguration {
}
