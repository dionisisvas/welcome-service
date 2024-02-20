package org.insurance.welcomeservice.config;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Contains configuration related to email sending.
 */
@Configuration
@EnableConfigurationProperties(EmailProperties.class)
public class EmailConfiguration {

  private static final String MAIL_TRANSPORT_PROTOCOL = "smtp";
  private static final String MAIL_SMTP_AUTH = "true";
  private static final String MAIL_SMTP_STARTTLS = "true";
  private static final String CONNECTION_TIMEOUT = "5000";

  private final EmailProperties emailProperties;

  public EmailConfiguration(EmailProperties emailProperties) {
    this.emailProperties = emailProperties;
  }

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(emailProperties.getHost());
    mailSender.setPort(emailProperties.getPort());

    mailSender.setUsername(emailProperties.getUsername());
    mailSender.setPassword(emailProperties.getPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", MAIL_TRANSPORT_PROTOCOL);
    props.put("mail.smtp.auth", MAIL_SMTP_AUTH);
    props.put("mail.smtp.starttls.enable", MAIL_SMTP_STARTTLS);
    props.put("mail.smtp.connectiontimeout", CONNECTION_TIMEOUT);
    props.put("mail.smtp.timeout", CONNECTION_TIMEOUT);
    props.put("mail.smtp.writetimeout", CONNECTION_TIMEOUT);
    props.put("mail.debug", emailProperties.getDebug());

    return mailSender;
  }

  @Bean
  public ExecutorService emailExecutorService() {
    return Executors.newWorkStealingPool();
  }
}
