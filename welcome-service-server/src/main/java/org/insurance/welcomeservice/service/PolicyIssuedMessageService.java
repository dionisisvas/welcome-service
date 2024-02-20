package org.insurance.welcomeservice.service;

import org.insurance.welcomeservice.rabbitmq.PolicyIssuedEventDto;

/**
 * Provides methods to handle PolicyIssuedMessages received from AMQP queue.
 */
public interface PolicyIssuedMessageService {

  void handleMessage(PolicyIssuedEventDto policyIssuedEventDto);
}
