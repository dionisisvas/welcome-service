package org.insurance.welcomeservice.exception;

/**
 * This exception is thrown when an email sending task has failed.
 */
public class EmailSendingFailedException extends RuntimeException {

  public EmailSendingFailedException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
