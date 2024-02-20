package org.insurance.welcomeservice.scheduler;

import static org.insurance.welcomeservice.TestHelper.buildWelcomeCall;
import static org.insurance.welcomeservice.model.WelcomeCallStatus.NO_ANSWER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutorService;
import org.insurance.welcomeservice.TestConfig;
import org.insurance.welcomeservice.config.EmailProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

@Import({TestConfig.class})
@SpringBootTest
@ActiveProfiles("test")
class EmailServiceTest {

  @MockBean
  private EmailProperties emailProperties;

  @MockBean
  private JavaMailSender javaMailSender;

  @Autowired
  private ExecutorService executorService;

  @Autowired
  private EmailService emailService;

  @Test
  void shouldSendWelcomeEmails() {
    // Given
    var givenFirst = buildWelcomeCall(1L, "policyRefId_newest", NO_ANSWER, null);
    var givenSecond = buildWelcomeCall(2L, "policyRefId_oldest", NO_ANSWER, null);
    var givenThird = buildWelcomeCall(3L, "policyRefId_middle", NO_ANSWER, null);
    var givenDelayedCalls = List.of(givenFirst, givenSecond, givenThird);
    doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
    when(emailProperties.getUsername()).thenReturn("welcome@insurance-company.com");
    when(emailProperties.getSubject()).thenReturn("Welcome!");

    // When
    emailService.sendWelcomeEmail(givenDelayedCalls);

    // Then
    verify(javaMailSender, times(givenDelayedCalls.size())).send(any(SimpleMailMessage.class));
  }

}
