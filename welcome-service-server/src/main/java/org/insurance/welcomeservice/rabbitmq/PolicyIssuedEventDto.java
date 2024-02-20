package org.insurance.welcomeservice.rabbitmq;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PolicyIssuedEventDto implements Serializable {
  private String policyReference;
  private String telephone;
  private String email;
  private LocalDateTime policyIssuedTimestamp;
}
