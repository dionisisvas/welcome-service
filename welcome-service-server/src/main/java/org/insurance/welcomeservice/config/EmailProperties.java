package org.insurance.welcomeservice.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Contains the mail configuration properties.
 */
@Data
@ConfigurationProperties("spring.mail")
@ToString
@Validated
public class EmailProperties {

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;

  @NotEmpty
  private String host;

  @NotNull
  private Integer port;

  @NotNull
  private String debug;

  @Value("${mail-properties.subject}")
  private String subject;
}
