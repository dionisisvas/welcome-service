package org.insurance.welcomeservice.service;

import static java.time.LocalDateTime.now;
import static org.insurance.welcomeservice.TestHelper.buildWelcomeCall;
import static org.insurance.welcomeservice.model.WelcomeCallStatus.NO_ANSWER;
import static org.insurance.welcomeservice.model.WelcomeCallStatus.PENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.insurance.welcomeservice.exception.NotFoundException;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.repository.WelcomeCallRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WelcomeCallServiceTest {

  @Mock
  private WelcomeCallRepository welcomeCallRepository;

  @InjectMocks
  private WelcomeCallServiceImpl welcomeCallService;

  @Captor
  private ArgumentCaptor<WelcomeCall> welcomeCallCaptor;

  @Test
  void shouldFindWelcomeCallByPolicyReference() {
    // Given
    var given = buildWelcomeCall();
    when(welcomeCallRepository.findByPolicyReference(given.getPolicyReference()))
        .thenReturn(Optional.of(given));

    // When
    Optional<WelcomeCall> actual = welcomeCallService.findWelcomeCallByPolicyReference(given.getPolicyReference());

    // Then
    verify(welcomeCallRepository).findByPolicyReference(given.getPolicyReference());
    assertTrue(actual.isPresent());
    assertEquals(given, actual.get());
  }

  @Test
  void shouldGetPendingWelcomeCallsByChronologicalOrder() {
    // Given
    var givenPolicyIssuedAtNewest = now();
    var givenPolicyIssuedAtOldest = givenPolicyIssuedAtNewest.minusHours(10);
    var givenPolicyIssuedAtMiddle = givenPolicyIssuedAtNewest.minusHours(6);

    // set the different policyIssuedAt attributes to validate the sorting
    var givenNewest = buildWelcomeCall(1L, "policyRefId_newest", PENDING, null);
    givenNewest.setPolicyIssuedAt(givenPolicyIssuedAtNewest);
    var givenOldest = buildWelcomeCall(2L, "policyRefId_oldest", PENDING, null);
    givenOldest.setPolicyIssuedAt(givenPolicyIssuedAtOldest);
    var givenMiddle = buildWelcomeCall(3L, "policyRefId_middle", PENDING, null);
    givenMiddle.setPolicyIssuedAt(givenPolicyIssuedAtMiddle);

    var given = List.of(givenNewest, givenOldest, givenMiddle);
    when(welcomeCallRepository.findByStatus(PENDING)).thenReturn(given);

    // When
    var actual = welcomeCallService.getPendingWelcomeCalls();

    // Then
    verify(welcomeCallRepository).findByStatus(PENDING);
    assertEquals(3, actual.size());
    assertEquals(actual.get(0), givenOldest);
    assertEquals(actual.get(1), givenMiddle);
    assertEquals(actual.get(2), givenNewest);
  }

  @Test
  void shouldGetNotAnsweredWelcomeCallsByChronologicalOrder() {
    // Given
    var givenPolicyIssuedAtNewest = now();
    var givenPolicyIssuedAtOldest = givenPolicyIssuedAtNewest.minusHours(10);
    var givenPolicyIssuedAtMiddle = givenPolicyIssuedAtNewest.minusHours(6);

    // set the different policyIssuedAt attributes to validate the sorting
    var givenNewest = buildWelcomeCall(1L, "policyRefId_newest", NO_ANSWER, null);
    givenNewest.setPolicyIssuedAt(givenPolicyIssuedAtNewest);
    var givenOldest = buildWelcomeCall(2L, "policyRefId_oldest", NO_ANSWER, null);
    givenOldest.setPolicyIssuedAt(givenPolicyIssuedAtOldest);
    var givenMiddle = buildWelcomeCall(3L, "policyRefId_middle", NO_ANSWER, null);
    givenMiddle.setPolicyIssuedAt(givenPolicyIssuedAtMiddle);

    var given = List.of(givenNewest, givenOldest, givenMiddle);
    when(welcomeCallRepository.findByStatusAndPolicyIssuedAtAfter(eq(NO_ANSWER), any()))
        .thenReturn(given);

    // When
    var actual = welcomeCallService.getNotAnsweredWelcomeCalls();

    // Then
    verify(welcomeCallRepository).findByStatusAndPolicyIssuedAtAfter(eq(NO_ANSWER), any());
    assertEquals(3, actual.size());
    assertEquals(actual.get(0), givenOldest);
    assertEquals(actual.get(1), givenMiddle);
    assertEquals(actual.get(2), givenNewest);
  }

  @Test
  void shouldUpdateWelcomeCall() {
    // Given
    var existing = buildWelcomeCall();
    var forUpdate = buildWelcomeCall(null, existing.getPolicyReference(), NO_ANSWER, existing.getAgentId());
    forUpdate.setStatus(NO_ANSWER);
    var expected = buildWelcomeCall();
    expected.setStatus(NO_ANSWER);
    when(welcomeCallRepository.findByPolicyReference(forUpdate.getPolicyReference()))
        .thenReturn(Optional.of(existing));
    when(welcomeCallRepository.save(any())).thenReturn(existing);

    // When
    var actual = welcomeCallService.updateWelcomeCall(forUpdate);

    // Then
    verify(welcomeCallRepository).findByPolicyReference(existing.getPolicyReference());
    verify(welcomeCallRepository).save(welcomeCallCaptor.capture());
    assertEquals(expected, welcomeCallCaptor.getValue());
  }

  @Test
  void shouldThrowNotFoundExceptionUpdateWelcomeCall_andWelcomeCallIsNotFound() {
    // Given
    var given = buildWelcomeCall();
    when(welcomeCallRepository.findByPolicyReference(given.getPolicyReference()))
        .thenReturn(Optional.empty());

    // When
    // Then
    assertThrows(NotFoundException.class, () ->
        welcomeCallService.updateWelcomeCall(given));
  }

}
