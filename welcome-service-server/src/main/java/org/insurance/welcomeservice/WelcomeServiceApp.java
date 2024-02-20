package org.insurance.welcomeservice;

import org.insurance.welcomeservice.config.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = AppConfiguration.class)
public class WelcomeServiceApp {

  public static void main(String[] args) {
    SpringApplication.run(WelcomeServiceApp.class, args);
  }
}