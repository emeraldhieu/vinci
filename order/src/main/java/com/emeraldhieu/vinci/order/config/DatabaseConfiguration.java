package com.emeraldhieu.vinci.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"com.emeraldhieu.vinci.order.logic"})
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfiguration {
}
