package org.insurance.welcomeservice.service;

import static java.util.Objects.nonNull;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.insurance.welcomeservice.mappers.WelcomeCallDtoMapper;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.model.WelcomeCallStatus;
import org.insurance.welcomeservice.rabbitmq.PolicyIssuedEventDto;
import org.insurance.welcomeservice.repository.WelcomeCallRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation class for PolicyIssuedMessageService.
 */
@Slf4j
@Service
public class PolicyIssuedMessageServiceImpl implements PolicyIssuedMessageService{

  private final WelcomeCallRepository welcomeCallRepository;
  private final WelcomeCallDtoMapper welcomeCallDtoMapper;

  public PolicyIssuedMessageServiceImpl(WelcomeCallRepository welcomeCallRepository,
      WelcomeCallDtoMapper welcomeCallDtoMapper) {
    this.welcomeCallRepository = welcomeCallRepository;
    this.welcomeCallDtoMapper = welcomeCallDtoMapper;
  }

  /**
   * Handles a received {@link PolicyIssuedEventDto} form the queue. First it will validate its
   * data then it will transform it to a domain object and finally save it in the database. If an
   * entry exists with the same policy reference it will update it and set the status to PENDING in
   * order to be processed by an agent.
   *
   * @param policyIssuedEventDto the received policy issued event message.
   */
  @Override
  public void handleMessage(PolicyIssuedEventDto policyIssuedEventDto) {
    if (!isMessageValid(policyIssuedEventDto)) {
      log.warn("Discarding message as it does not have all mandatory parameters : {}",
          policyIssuedEventDto);
      return;
    }
    final WelcomeCall welcomeCall = welcomeCallDtoMapper.toWelcomeCall(policyIssuedEventDto);

    Optional<WelcomeCall> existingWelcomeCall
        = welcomeCallRepository.findByPolicyReference(policyIssuedEventDto.getPolicyReference());
    if (existingWelcomeCall.isPresent()) {
      WelcomeCall updatedWelcomeCall = updateWelcomeCall(welcomeCall, existingWelcomeCall.get());
      log.info("WelcomeCall entry has been updated: {}", updatedWelcomeCall);
    } else {
      WelcomeCall newWelcomeCall = welcomeCallRepository.save(welcomeCall);
      log.info("New WelcomeCall entry has been saved: {}", newWelcomeCall);
    }
  }

  /**
   * Will validate message to check all mandatory fields are present.
   *
   * @param policyIssuedEventDto the message to validate.
   * @return true if message is valid, false otherwise.
   */
  private boolean isMessageValid(PolicyIssuedEventDto policyIssuedEventDto) {
    return StringUtils.isNotBlank(policyIssuedEventDto.getPolicyReference())
        && StringUtils.isNotBlank(policyIssuedEventDto.getTelephone())
        && StringUtils.isNotBlank(policyIssuedEventDto.getEmail())
        && nonNull(policyIssuedEventDto.getPolicyIssuedTimestamp());
  }

  private WelcomeCall updateWelcomeCall(WelcomeCall newWelcomeCall, WelcomeCall existingWelcomeCall) {
    log.info("A WelcomeCall entry already exists with policy reference {} will do an update instead "
        + "and set status to PENDING", newWelcomeCall.getPolicyReference());
    final WelcomeCall welcomeCallForUpdate = WelcomeCall.builder()
        .id(existingWelcomeCall.getId())
        .telephone(newWelcomeCall.getTelephone())
        .email(newWelcomeCall.getEmail())
        .policyIssuedAt(newWelcomeCall.getPolicyIssuedAt())
        .status(WelcomeCallStatus.PENDING)
        .agentId(null)
        .build();
    return welcomeCallRepository.save(welcomeCallForUpdate);
  }
}
