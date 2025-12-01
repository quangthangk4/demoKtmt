package com.ktmt.demoapplication.user.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA Configuration
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for JPA configuration
 * - Separation of Concerns: Infrastructure configuration separated from business logic
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.ktmt.demoapplication.user.infrastructure.persistence.repository")
@EnableTransactionManagement
public class JpaConfig {
    // JPA configuration can be extended here if needed
}
