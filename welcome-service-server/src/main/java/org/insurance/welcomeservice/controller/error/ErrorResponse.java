package org.insurance.welcomeservice.controller.error;

import lombok.Builder;
import lombok.Value;

/**
 * Error Response class.
 */
@Value
@Builder
public class ErrorResponse {
  String errorCode;
  String message;
}
