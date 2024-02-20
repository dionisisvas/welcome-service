package org.insurance.welcomeservice.config;

import org.insurance.welcomeservice.scheduler.DelayedCallsScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuration class for scheduled email sending.
 * The scheduler can be disabled by setting the property `scheduler.enabled` to false.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name="delayed-calls-scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class ScheduledJobConfiguration {

  private final Runnable delayedCallsScheduler;

  public ScheduledJobConfiguration(DelayedCallsScheduler delayedCallsScheduler) {
    this.delayedCallsScheduler = delayedCallsScheduler;
  }

  @Scheduled(cron = "${delayed-calls-scheduler.cron}")
  public void delayedCallsScheduler() {
    delayedCallsScheduler.run();
  }
}
