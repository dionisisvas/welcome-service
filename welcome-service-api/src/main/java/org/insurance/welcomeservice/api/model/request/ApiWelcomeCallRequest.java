package org.insurance.welcomeservice.api.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.insurance.welcomeservice.api.model.ApiWelcomeCallStatus;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class ApiWelcomeCallRequest {
  private String policyReference;
  private ApiWelcomeCallStatus status;
  private String agentId;
}
