package org.insurance.welcomeservice.service;

import java.util.List;
import java.util.Optional;
import org.insurance.welcomeservice.model.WelcomeCall;

/**
 * Provides methods for updating and fetching and handling {@link WelcomeCall} domain objects.
 */
public interface WelcomeCallService {

  List<WelcomeCall> getPendingWelcomeCalls();

  List<WelcomeCall> getNotAnsweredWelcomeCalls();

  List<WelcomeCall> getDelayedWelcomeCalls();

  WelcomeCall updateWelcomeCall(WelcomeCall welcomeCall);

  Optional<WelcomeCall> findWelcomeCallByPolicyReference(String policyReference);

  void updateProcessedDelayedWelcomeCalls(List<WelcomeCall> welcomeCalls);
}
