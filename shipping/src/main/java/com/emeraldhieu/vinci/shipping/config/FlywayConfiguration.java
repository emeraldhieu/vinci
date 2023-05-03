package com.emeraldhieu.vinci.shipping.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FlywayConfiguration {

    /**
     * Drop everything and recreate the entire database.
     * See https://stackoverflow.com/questions/42314331/flyway-db-migration-with-hibernate-create-drop#42314420
     * See https://www.baeldung.com/database-migrations-with-flyway#3-empty-flywaymigrationstrategy
     */
    @Bean
    @Profile("local | k8s")
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        FlywayMigrationStrategy strategy = flyway -> {
            flyway.clean();
            flyway.migrate();
        };
        return strategy;
    }
}
