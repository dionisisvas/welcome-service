package org.insurance.welcomeservice.exception;

/**
 * Exception that is thrown when a welcome call is being processed by another agent.
 */
public class WelcomeCallProcessedException extends RuntimeException {

  public WelcomeCallProcessedException(String errorMessage) {
    super(errorMessage);
  }

  public static WelcomeCallProcessedException ofBeingProcessedByAnotherAgent() {
    throw new WelcomeCallProcessedException("Welcome Call is being processed by another call center agent.");
  }
}
