package org.insurance.welcomeservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class TestConfig {

  @Bean
  public ExecutorService executorService() {
    return Executors.newFixedThreadPool(1);
  }

}
