package org.insurance.welcomeservice.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"org.insurance.welcomeservice.model"})
@ComponentScan(basePackages = {"org.insurance.welcomeservice.repository"})
@EnableJpaRepositories(basePackages = {"org.insurance.welcomeservice.repository"})
public class JpaConfiguration {
}
