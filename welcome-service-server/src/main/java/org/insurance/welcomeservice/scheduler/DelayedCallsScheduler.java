package org.insurance.welcomeservice.scheduler;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.service.WelcomeCallService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Runs the scheduled task that handles delayed calls.
 */
@Slf4j
@Component
public class DelayedCallsScheduler implements Runnable {

  private final WelcomeCallService welcomeCallService;
  private final EmailService emailService;

  public DelayedCallsScheduler(WelcomeCallService welcomeCallService, EmailService emailService) {
    this.welcomeCallService = welcomeCallService;
    this.emailService = emailService;
  }

  /**
   * Runs the scheduled job for delayed calls.
   */
  @Override
  @Transactional
  public void run() {
    try {
      List<WelcomeCall> delayedWelcomeCalls = welcomeCallService.getDelayedWelcomeCalls();
      if (delayedWelcomeCalls.isEmpty()) {
        log.info("No delayed calls where found for processing.");
        return;
      }
      emailService.sendWelcomeEmail(delayedWelcomeCalls);
      welcomeCallService.updateProcessedDelayedWelcomeCalls(delayedWelcomeCalls);
    } catch (Exception e) {
      log.error("An error occurred while trying to process the delayed welcome calls {}", e.getMessage());
    }
  }
}
