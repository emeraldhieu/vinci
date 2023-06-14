package com.emeraldhieu.vinci.shipping.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

/**
 * A class that sets current user for an entity.
 * It can be improved to get current user from Spring Security.
 * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing.auditor-aware
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // TODO Get user from SecurityContextHolder when Spring Security is implemented.
        String userId = UUID.randomUUID().toString().replace("-", "");
        return Optional.of(userId);
    }
}