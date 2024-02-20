package org.insurance.welcomeservice.scheduler;

import static org.insurance.welcomeservice.TestHelper.buildWelcomeCall;
import static org.insurance.welcomeservice.model.WelcomeCallStatus.NO_ANSWER;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.service.WelcomeCallService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = {DelayedCallsScheduler.class})
class DelayedCallsSchedulerTest {

  @MockBean
  private WelcomeCallService welcomeCallService;

  @MockBean
  private EmailService emailService;

  @Autowired
  private DelayedCallsScheduler delayedCallsScheduler;

  @Captor
  private ArgumentCaptor<List<WelcomeCall>> welcomeCallListCaptor;

  @Test
  void shouldRun() {
    // Given
    var givenFirst = buildWelcomeCall(1L, "policyRefId_newest", NO_ANSWER, null);
    var givenSecond = buildWelcomeCall(2L, "policyRefId_oldest", NO_ANSWER, null);
    var givenThird = buildWelcomeCall(3L, "policyRefId_middle", NO_ANSWER, null);

    var givenDelayedCalls = List.of(givenFirst, givenSecond, givenThird);
    when(welcomeCallService.getDelayedWelcomeCalls()).thenReturn(givenDelayedCalls);
    doNothing().when(welcomeCallService).updateProcessedDelayedWelcomeCalls(givenDelayedCalls);
    doNothing().when(emailService).sendWelcomeEmail(givenDelayedCalls);

    // When
    delayedCallsScheduler.run();

    // Then
    verify(welcomeCallService).getDelayedWelcomeCalls();
    verify(emailService).sendWelcomeEmail(givenDelayedCalls);
    verify(welcomeCallService).updateProcessedDelayedWelcomeCalls(givenDelayedCalls);
  }
}
