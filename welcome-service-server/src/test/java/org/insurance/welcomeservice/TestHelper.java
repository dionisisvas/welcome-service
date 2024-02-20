package org.insurance.welcomeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.insurance.welcomeservice.api.model.ApiWelcomeCallStatus;
import org.insurance.welcomeservice.api.model.request.ApiWelcomeCallRequest;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.model.WelcomeCallStatus;
import org.insurance.welcomeservice.rabbitmq.PolicyIssuedEventDto;

public final class TestHelper {

  private static final String TEST_EMAIL = "test@test.com";
  private static final String TEST_TELEPHONE = "6912345678";
  private static final String TEST_POLICY_REFERENCE = "test_policy_reference";
  private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
  private static final LocalDateTime POLICY_ISSUED_AT =
      LocalDateTime.parse("2024-02-17T23:15:45.456", DATE_FORMATTER);
  private static final String TEST_AGENT_ID = "test_agent_id";

  public static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_PATTERN));
    return objectMapper;
  }

  public static WelcomeCall buildWelcomeCall() {
    return WelcomeCall.builder()
        .id(1L)
        .policyReference(TEST_POLICY_REFERENCE)
        .policyIssuedAt(POLICY_ISSUED_AT)
        .status(WelcomeCallStatus.ANSWERED)
        .telephone(TEST_TELEPHONE)
        .email(TEST_EMAIL)
        .agentId(TEST_AGENT_ID)
        .build();
  }

  public static WelcomeCall buildWelcomeCall(Long id, String policyReference, WelcomeCallStatus status, String agentId) {
    return WelcomeCall.builder()
        .id(id)
        .policyReference(policyReference)
        .policyIssuedAt(POLICY_ISSUED_AT)
        .status(status)
        .telephone(TEST_TELEPHONE)
        .email(TEST_EMAIL)
        .agentId(agentId)
        .build();
  }

  public static ApiWelcomeCallRequest buildWelcomeCallRequest() {
    return ApiWelcomeCallRequest.builder()
        .status(ApiWelcomeCallStatus.PENDING)
        .agentId(TEST_AGENT_ID)
        .build();
  }

  public static PolicyIssuedEventDto buildPolicyIssuedEventDto() {
    return PolicyIssuedEventDto.builder()
        .policyReference(TEST_POLICY_REFERENCE)
        .telephone(TEST_TELEPHONE)
        .email(TEST_EMAIL)
        .policyIssuedTimestamp(POLICY_ISSUED_AT)
        .build();
  }

  public static PolicyIssuedEventDto buildPolicyIssuedEventDto(String policyReference, String telephone,
      String email) {
    return PolicyIssuedEventDto.builder()
        .policyReference(policyReference)
        .telephone(telephone)
        .email(email)
        .policyIssuedTimestamp(POLICY_ISSUED_AT)
        .build();
  }
}
