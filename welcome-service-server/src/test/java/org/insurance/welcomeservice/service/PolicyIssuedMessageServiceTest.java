package org.insurance.welcomeservice.service;

import static org.insurance.welcomeservice.TestHelper.buildPolicyIssuedEventDto;
import static org.insurance.welcomeservice.TestHelper.buildWelcomeCall;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.insurance.welcomeservice.mappers.WelcomeCallDtoMapperImpl;
import org.insurance.welcomeservice.repository.WelcomeCallRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {PolicyIssuedMessageServiceImpl.class})
class PolicyIssuedMessageServiceTest {

  @MockBean
  private WelcomeCallRepository welcomeCallRepository;

  @MockBean
  private WelcomeCallDtoMapperImpl mapper;

  @Autowired
  private PolicyIssuedMessageServiceImpl service;

  @Test
  void shouldHandleNewMessage() {
    // Given
    var given = buildPolicyIssuedEventDto();
    when(welcomeCallRepository.findByPolicyReference(given.getPolicyReference()))
        .thenReturn(Optional.empty());
    when(mapper.toWelcomeCall(given)).thenCallRealMethod();

    // When
    service.handleMessage(given);

    // Then
    verify(welcomeCallRepository).findByPolicyReference(given.getPolicyReference());
    verify(mapper).toWelcomeCall(given);
    verify(welcomeCallRepository).save(any());
    verifyNoMoreInteractions(welcomeCallRepository);
  }

  @Test
  void shouldHandleNewMessageAndUpdate_ifAlreadyPresent() {
    // Given
    var given = buildPolicyIssuedEventDto();
    var givenWelcomeCall = buildWelcomeCall();
    when(welcomeCallRepository.findByPolicyReference(given.getPolicyReference()))
        .thenReturn(Optional.of(givenWelcomeCall));
    when(mapper.toWelcomeCall(given)).thenCallRealMethod();

    // When
    service.handleMessage(given);

    // Then
    verify(welcomeCallRepository).findByPolicyReference(given.getPolicyReference());
    verify(mapper).toWelcomeCall(given);
    verify(welcomeCallRepository).save(any());
    verifyNoMoreInteractions(welcomeCallRepository);
  }


  @ParameterizedTest
  @CsvSource(value = {
      "null,6912345678,test@test.com",
      "testPolicyRef,null,test@test.com",
      "testPolicyRef,6912345678,null",
  }, nullValues = {"null"})
  void shouldDiscardMessage_ifAnyMandatoryFieldIsNull(String policyReference, String telephone, String email) {
    // Given
    var given = buildPolicyIssuedEventDto(policyReference, telephone, email);

    // When
    service.handleMessage(given);

    // Then
    verifyNoInteractions(welcomeCallRepository);
    verifyNoInteractions(mapper);
  }
}
