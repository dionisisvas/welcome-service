package org.insurance.welcomeservice.repository;


import java.time.LocalDateTime;
import org.insurance.welcomeservice.config.JpaConfiguration;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.model.WelcomeCallStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = { JpaConfiguration.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class WelcomeCallRepositoryTest {

  @Autowired
  private WelcomeCallRepository welcomeCallRepository;

  @Test
  void shouldSaveWelcomeCallSuccessfully() {
    // Given
    var givenWelcomeCall = buildWelcomeCall(WelcomeCallStatus.PENDING, "testPolicyRef");

    // When
    var actual = welcomeCallRepository.save(givenWelcomeCall);

    // Then
    assertNotNull(actual);
    assertNotNull(actual.getId());
    assertEquals(actual.getEmail(), givenWelcomeCall.getEmail());
    assertEquals(actual.getTelephone(), givenWelcomeCall.getTelephone());
    assertEquals(actual.getStatus(), givenWelcomeCall.getStatus());
    assertEquals(actual.getPolicyReference(), givenWelcomeCall.getPolicyReference());
    assertEquals(actual.getPolicyIssuedAt(), givenWelcomeCall.getPolicyIssuedAt());
    assertNotNull(actual.getCreatedAt());
    assertNotNull(actual.getUpdatedAt());
  }

  @Test
  void shouldFindByStatusSuccessfully() {
    // Given
    var givenPendingWelcomeCall = buildWelcomeCall(WelcomeCallStatus.PENDING, "testPolicyRef1");
    var givenPendingWelcomeCall2 = buildWelcomeCall(WelcomeCallStatus.PENDING, "testPolicyRef2");
    var givenAnsweredWelcomeCall = buildWelcomeCall(WelcomeCallStatus.ANSWERED, "testPolicyRef3");
    welcomeCallRepository.save(givenPendingWelcomeCall);
    welcomeCallRepository.save(givenPendingWelcomeCall2);
    welcomeCallRepository.save(givenAnsweredWelcomeCall);

    // When
    var actual = welcomeCallRepository.findByStatus(WelcomeCallStatus.PENDING);

    // Then
    assertNotNull(actual);
    assertEquals(2, actual.size());
  }

  @Test
  void shouldFindByStatusAndPolicyIssuedAtBeforeSuccessfully() {
    // Given
    var givenWelcomeCallBefore = buildWelcomeCall(WelcomeCallStatus.NO_ANSWER, "testPolicyRef1");
    givenWelcomeCallBefore.setPolicyIssuedAt(LocalDateTime.of(2024,2,12,9, 23,20));
    var givenWelcomeCallAfter = buildWelcomeCall(WelcomeCallStatus.NO_ANSWER, "testPolicyRef2");
    var givenAnsweredWelcomeCall = buildWelcomeCall(WelcomeCallStatus.ANSWERED, "testPolicyRef3");
    welcomeCallRepository.save(givenWelcomeCallBefore);
    welcomeCallRepository.save(givenWelcomeCallAfter);
    welcomeCallRepository.save(givenAnsweredWelcomeCall);

    var givenDate = LocalDateTime.of(2024,2,14,9, 23,20);
    assertTrue(givenDate.isAfter(givenWelcomeCallBefore.getPolicyIssuedAt()));

    // When
    var actualList = welcomeCallRepository
        .findByStatusAndPolicyIssuedAtBefore(WelcomeCallStatus.NO_ANSWER, givenDate);

    // Then
    assertNotNull(actualList);
    assertEquals(1, actualList.size());
    var actual = actualList.get(0);
    assertEquals(actual.getPolicyIssuedAt(), givenWelcomeCallBefore.getPolicyIssuedAt());
  }

  @Test
  void shouldFindByStatusAndPolicyIssuedAtAfterSuccessfully() {
    // Given
    var givenWelcomeCallBefore = buildWelcomeCall(WelcomeCallStatus.NO_ANSWER, "testPolicyRef1");
    givenWelcomeCallBefore.setPolicyIssuedAt(LocalDateTime.of(2024,2,12,9, 23,20));
    var givenWelcomeCallAfter = buildWelcomeCall(WelcomeCallStatus.NO_ANSWER, "testPolicyRef2");
    var givenAnsweredWelcomeCall = buildWelcomeCall(WelcomeCallStatus.ANSWERED, "testPolicyRef3");
    welcomeCallRepository.save(givenWelcomeCallBefore);
    welcomeCallRepository.save(givenWelcomeCallAfter);
    welcomeCallRepository.save(givenAnsweredWelcomeCall);

    var givenDate = LocalDateTime.of(2024,2,14,9, 23,20);
    assertTrue(givenDate.isBefore(givenWelcomeCallAfter.getPolicyIssuedAt()));

    // When
    var actualList = welcomeCallRepository
        .findByStatusAndPolicyIssuedAtAfter(WelcomeCallStatus.NO_ANSWER, givenDate);

    // Then
    assertNotNull(actualList);
    assertEquals(1, actualList.size());
    var actual = actualList.get(0);
    assertEquals(actual.getPolicyIssuedAt(), givenWelcomeCallAfter.getPolicyIssuedAt());
  }

  private WelcomeCall buildWelcomeCall(WelcomeCallStatus status, String policyReference) {
    return WelcomeCall.builder()
        .id(null)
        .email("test@test.com")
        .telephone("6912345678")
        .status(status)
        .policyReference(policyReference)
        .policyIssuedAt(LocalDateTime.now())
        .agentId(null)
        .build();
  }
}