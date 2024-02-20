package org.insurance.welcomeservice.exception;

/**
 * Exception that is thrown when a resource is not found.
 */
public class NotFoundException extends RuntimeException {

  public NotFoundException(String errorMessage) {
    super(errorMessage);
  }

  public static NotFoundException ofWelcomeCallNotFound() {
    throw new NotFoundException("Welcome Call entry cannot be found.");
  }
}
