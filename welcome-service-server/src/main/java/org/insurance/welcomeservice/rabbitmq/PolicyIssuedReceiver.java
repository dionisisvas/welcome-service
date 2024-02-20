package org.insurance.welcomeservice.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.insurance.welcomeservice.service.PolicyIssuedMessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Receives and handles PolicyIssuedEventDto from a RabbitMQ queue.
 */
@Slf4j
@Component
public class PolicyIssuedReceiver {

  private final String queueName;
  private final PolicyIssuedMessageService policyIssuedMessageService;

  public PolicyIssuedReceiver(@Value("${rabbitmq.queue}") String queueName,
      PolicyIssuedMessageService policyIssuedMessageService) {
    this.queueName = queueName;
    this.policyIssuedMessageService = policyIssuedMessageService;
  }

  @RabbitListener(queues = "${rabbitmq.queue}")
  public void receiveMessage(final PolicyIssuedEventDto receivedMessage) {
    log.info("Received message from queue {}: {}", queueName, receivedMessage);
    policyIssuedMessageService.handleMessage(receivedMessage);
  }
}
