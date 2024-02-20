package org.insurance.welcomeservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
    "org.insurance.welcomeservice.controller",
    "org.insurance.welcomeservice.service",
    "org.insurance.welcomeservice.scheduler",
    "org.insurance.welcomeservice.mappers"
})
@Import({JpaConfiguration.class})
public class AppConfiguration {
}
