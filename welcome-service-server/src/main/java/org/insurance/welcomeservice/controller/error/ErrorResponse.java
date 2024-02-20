package org.insurance.welcomeservice.controller.error;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Error Response class.
 */
@Data
@Builder
@EqualsAndHashCode
@ToString
public class ErrorResponse {
  String errorCode;
  String message;
}
