package org.insurance.welcomeservice.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.insurance.welcomeservice.exception.NotFoundException;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.model.WelcomeCallStatus;
import org.insurance.welcomeservice.repository.WelcomeCallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for WelcomeCallService.
 */
@Service
public class WelcomeCallServiceImpl implements WelcomeCallService {

  private final WelcomeCallRepository welcomeCallRepository;

  public WelcomeCallServiceImpl(WelcomeCallRepository welcomeCallRepository) {
    this.welcomeCallRepository = welcomeCallRepository;
  }

  /**
   * Returns all pending welcome calls by chronological order (oldest first).
   * Pending calls are those that have status PENDING and are not assigned to an agent (ie the
   * agent id is null).
   *
   * @return a list with pending WelcomeCall in chronological order.
   */
  @Override
  @Transactional
  public List<WelcomeCall> getPendingWelcomeCalls() {
    return welcomeCallRepository.findByStatus(WelcomeCallStatus.PENDING).stream()
        .sorted(Comparator.comparing(WelcomeCall::getPolicyIssuedAt))
        .toList();
  }

  /**
   * Updates fields of an existing welcome call entry.
   *
   * @param welcomeCall the Welcome Call entry to update.
   * @return  the updated entry.
   */
  @Override
  @Transactional
  public WelcomeCall updateWelcomeCall(WelcomeCall welcomeCall) {
    WelcomeCall existingWelcomeCall = welcomeCallRepository.findByPolicyReference(welcomeCall.getPolicyReference())
        .orElseThrow(NotFoundException::ofWelcomeCallNotFound);
    WelcomeCall welcomeCallForUpdate = prepareForUpdate(welcomeCall, existingWelcomeCall);
    return welcomeCallRepository.save(welcomeCallForUpdate);
  }

  /**
   * Returns all not answered welcome calls by chronological order which have status NO_ANSWER and
   * two days have not passed since the policy was issued.
   *
   * @return a list with not answered WelcomeCall in chronological order.
   */
  @Override
  @Transactional
  public List<WelcomeCall> getNotAnsweredWelcomeCalls() {
    List<WelcomeCall> welcomeCalls =
        welcomeCallRepository.findByStatusAndPolicyIssuedAtAfter(WelcomeCallStatus.NO_ANSWER, getDelayedDate());
    return welcomeCalls.stream()
        .sorted(Comparator.comparing(WelcomeCall::getPolicyIssuedAt))
        .toList();

  }

  /**
   * Finds a welcome call by policy reference.
   *
   * @param policyReference the policy reference.
   * @return the welcome call optional.
   */
  @Override
  @Transactional
  public Optional<WelcomeCall> findWelcomeCallByPolicyReference(String policyReference) {
    return welcomeCallRepository.findByPolicyReference(policyReference);
  }

  /**
   * We prepare the welcome call to be updated in the DB by keeping all it's attributes, except
   * the status and the agent id.
   *
   * @param newWelcomeCall the welcome call which contains the attributes to be updated.
   * @param existingWelcomeCall the existing welcome call entry.
   * @return the welcome call to be updated.
   */
  private WelcomeCall prepareForUpdate(WelcomeCall newWelcomeCall, WelcomeCall existingWelcomeCall) {
    return WelcomeCall.builder()
        .id(existingWelcomeCall.getId())
        .email(existingWelcomeCall.getEmail())
        .telephone(existingWelcomeCall.getTelephone())
        .policyIssuedAt(existingWelcomeCall.getPolicyIssuedAt())
        .policyReference(existingWelcomeCall.getPolicyReference())
        .status(newWelcomeCall.getStatus())
        .agentId(newWelcomeCall.getAgentId())
        .build();
  }

  /**
   * Calculates and returns the delayed date. A welcome call is considered delayed if two days
   * have passed since the policy was issued.
   *
   * @return the delayed date (current LocalDateTime minus two days).
   */
  private LocalDateTime getDelayedDate() {
    return LocalDateTime.now().minusDays(2);
  }
}
