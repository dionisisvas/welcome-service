package org.insurance.welcomeservice.scheduler;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.insurance.welcomeservice.config.EmailProperties;
import org.insurance.welcomeservice.exception.EmailSendingFailedException;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 * Handles the sending of emails for delayed calls.
 */
@Slf4j
@Service
public class EmailService {

  @Value("classpath:templates/welcome-email-template.txt")
  private Resource emailTemplateResource;

  private final JavaMailSender javaMailSender;
  private final EmailProperties emailProperties;
  private final ExecutorService emailExecutorService;

  public EmailService(JavaMailSender javaMailSender, EmailProperties emailProperties,
      ExecutorService emailExecutorService) {
    this.javaMailSender = javaMailSender;
    this.emailProperties = emailProperties;
    this.emailExecutorService = emailExecutorService;
  }

  /**
   * Sends the welcome emails asynchronously.
   *
   * @param welcomeCalls the delayed welcome calls list.
   */
  public void sendWelcomeEmail(final List<WelcomeCall> welcomeCalls) {
    List<CompletableFuture> emailSendingTasks = new ArrayList<>(welcomeCalls.size());
    for (WelcomeCall welcomeCall : welcomeCalls)  {
      CompletableFuture<Void> emailTask = CompletableFuture.runAsync(() ->
          sendEmailFromWelcomeCall(welcomeCall), emailExecutorService);
      emailSendingTasks.add(emailTask);
    }
    CompletableFuture.allOf(emailSendingTasks.toArray(new CompletableFuture[0]))
        .exceptionally(e -> {
          log.error("Error while sending welcome emails asynchronously: {}", e.getMessage());
          throw new EmailSendingFailedException(e.getMessage(), e.getCause());
        }).join();
    log.info("Completed sending {} welcome emails for delayed calls.", welcomeCalls.size());
  }

  private void sendEmailFromWelcomeCall(WelcomeCall welcomeCall) {
    SimpleMailMessage mailMessage = prepareWelcomeEmail(welcomeCall.getEmail(),
        emailProperties.getUsername(), getMailTemplate());
    try {
      log.info("Sending welcome email: {}", mailMessage);
      javaMailSender.send(mailMessage);
    } catch (MailException e) {
      log.error("Error while sending welcome email: {}", mailMessage);
    }

  }

  private SimpleMailMessage prepareWelcomeEmail(String to, String from, String body) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(to);
    mailMessage.setFrom(from);
    mailMessage.setSubject(emailProperties.getSubject());
    mailMessage.setText(body);
    return mailMessage;
  }

  private String getMailTemplate() {
    try (Reader reader = new InputStreamReader(emailTemplateResource.getInputStream(), UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      log.error("Error while creating email template to send: {}", e.getMessage());
      throw new EmailSendingFailedException(e.getMessage(), e.getCause());
    }
  }
}
