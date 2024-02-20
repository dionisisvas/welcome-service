package org.insurance.welcomeservice.rabbitmq;

import static org.insurance.welcomeservice.TestHelper.buildPolicyIssuedEventDto;
import static org.mockito.Mockito.verify;

import org.insurance.welcomeservice.service.PolicyIssuedMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PolicyIssuedReceiverTest {

  @Mock
  private PolicyIssuedMessageService service;

  @InjectMocks
  private PolicyIssuedReceiver policyIssuedReceiver;

  @Test
  void shouldReceiveAndHandleMessage() {
    // Given
    var given = buildPolicyIssuedEventDto();

    // When
    policyIssuedReceiver.receiveMessage(given);

    // Then
    verify(service).handleMessage(given);
  }
}
