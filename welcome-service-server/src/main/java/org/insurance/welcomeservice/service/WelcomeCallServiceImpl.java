package org.insurance.welcomeservice.service;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.insurance.welcomeservice.exception.NotFoundException;
import org.insurance.welcomeservice.exception.WelcomeCallProcessedException;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.model.WelcomeCallStatus;
import org.insurance.welcomeservice.repository.WelcomeCallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for WelcomeCallService.
 */
@Slf4j
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
     List<WelcomeCall> welcomeCalls = welcomeCallRepository.findByStatus(WelcomeCallStatus.PENDING).stream()
        .sorted(Comparator.comparing(WelcomeCall::getPolicyIssuedAt))
        .toList();
     log.info("Fetched all pending welcome calls: {}", welcomeCalls);
     return welcomeCalls;
  }

  /**
   * Updates fields of an existing welcome call entry (status and agentId fields).
   *
   * @param welcomeCall the Welcome Call entry to update.
   * @return  the updated entry.
   */
  @Override
  @Transactional
  public WelcomeCall updateWelcomeCall(WelcomeCall welcomeCall) {
    WelcomeCall existingWelcomeCall = welcomeCallRepository.findByPolicyReference(welcomeCall.getPolicyReference())
        .orElseThrow(NotFoundException::ofWelcomeCallNotFound);
    verifyWelcomeCallProcessingStatus(existingWelcomeCall, welcomeCall.getAgentId());
    WelcomeCall welcomeCallForUpdate = prepareForUpdate(welcomeCall, existingWelcomeCall);
    log.info("Updating welcome call entry: {}", welcomeCallForUpdate);
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
    log.info("Fetched all not answered welcome calls within the last two days: {}", welcomeCalls);
    return welcomeCalls.stream()
        .sorted(Comparator.comparing(WelcomeCall::getPolicyIssuedAt))
        .toList();
  }

  /**
   * Returns all delayed welcome calls by chronological order, which have status NO_ANSWER and
   * two days have passed since the policy was issued.
   *
   * @return a list with not answered WelcomeCall in chronological order.
   */
  @Override
  @Transactional
  public List<WelcomeCall> getDelayedWelcomeCalls() {
    List<WelcomeCall> welcomeCalls =
        welcomeCallRepository.findByStatusAndPolicyIssuedAtBefore(WelcomeCallStatus.NO_ANSWER, getDelayedDate());
    log.info("Fetched all not answered welcome calls within the last two days: {}", welcomeCalls);
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
   * Sets the status of the welcome calls to EMAIL_SENT and updates them in the database.
   *
   * @param delayedWelcomeCalls the welcome calls to be updated.
   */
  @Override
  @Transactional
  public void updateProcessedDelayedWelcomeCalls(List<WelcomeCall> delayedWelcomeCalls) {
    List<WelcomeCall> processedDelayedWelcomeCalls =
        delayedWelcomeCalls.stream().map(this::getProcessedWelcomeCall).toList();
    welcomeCallRepository.saveAll(processedDelayedWelcomeCalls);
    log.info("Completed processing of delayed welcome calls: {}", processedDelayedWelcomeCalls);
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
        .createdAt(existingWelcomeCall.getCreatedAt())
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

  /**
   * Verifies if the welcome call is being processed by another agent at this time.
   *
   * @param welcomeCall the welcome call entry.
   */
  private void verifyWelcomeCallProcessingStatus(WelcomeCall welcomeCall, String newAgentId) {
    // if agentId is set and is not the same as the one making the update request,
    // then it means another agent is handling this welcome call.
    if (!isNull(welcomeCall.getAgentId())
        && !Objects.equals(welcomeCall.getAgentId(), newAgentId) ) {
      log.error("Welcome call is being handled by another call center agent: {}", welcomeCall);
      throw WelcomeCallProcessedException.ofBeingProcessedByAnotherAgent();
    }
  }

  private WelcomeCall getProcessedWelcomeCall(WelcomeCall welcomeCall) {
    return WelcomeCall.builder()
        .id(welcomeCall.getId())
        .agentId(welcomeCall.getAgentId())
        .email(welcomeCall.getEmail())
        .telephone(welcomeCall.getTelephone())
        .policyIssuedAt(welcomeCall.getPolicyIssuedAt())
        .policyReference(welcomeCall.getPolicyReference())
        .createdAt(welcomeCall.getCreatedAt())
        .status(WelcomeCallStatus.EMAIL_SENT)
        .build();
  }
}
