package org.insurance.welcomeservice.controller;

import static org.insurance.welcomeservice.TestHelper.buildWelcomeCall;
import static org.insurance.welcomeservice.TestHelper.buildWelcomeCallRequest;
import static org.insurance.welcomeservice.TestHelper.getObjectMapper;
import static org.insurance.welcomeservice.model.WelcomeCallStatus.PENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.insurance.welcomeservice.api.model.ApiWelcomeCallStatus;
import org.insurance.welcomeservice.mappers.WelcomeCallApiMapper;
import org.insurance.welcomeservice.model.WelcomeCallStatus;
import org.insurance.welcomeservice.service.WelcomeCallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
class WelcomeCallControllerTest {

  private static final String BASE_URL = "/v1/welcomecalls";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WelcomeCallApiMapper mapper;

  @MockBean
  private WelcomeCallService welcomeCallService;

  private final ObjectMapper objectMapper = getObjectMapper();

  @Test
  void shouldGetPendingWelcomeCalls() throws Exception {
    // Given
    var givenWelcomeCalls = List.of(
        buildWelcomeCall(1L, "policyRef1", PENDING, null),
        buildWelcomeCall(2L, "policyRef2", PENDING, null));
    var expectedResponse = objectMapper.writeValueAsString(mapper.toApiWelcomeCallResponseList(givenWelcomeCalls));
    when(welcomeCallService.getPendingWelcomeCalls()).thenReturn(givenWelcomeCalls);

    // When
    String response = mockMvc
        .perform(get(BASE_URL+"/pending")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn()
        .getResponse().getContentAsString();

    // Then
    verify(welcomeCallService).getPendingWelcomeCalls();
    assertEquals(response, expectedResponse);
  }

  @Test
  void shouldUpdateWelcomeCall() throws Exception {
    // Given
    var givenRequest = buildWelcomeCallRequest();
    var givenWelcomeCall = mapper.toWelcomeCallModel(givenRequest);
    var expectedResponse = objectMapper.writeValueAsString(mapper.toApiWelcomeCallResponse(givenWelcomeCall));
    when(welcomeCallService.updateWelcomeCall(givenWelcomeCall)).thenReturn(givenWelcomeCall);

    // When
    String response = mockMvc
        .perform(patch(BASE_URL+"/welcomecall")
            .contentType(MediaType.APPLICATION_JSON)
            .content(expectedResponse))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn()
        .getResponse().getContentAsString();

    // Then
    verify(welcomeCallService).updateWelcomeCall(givenWelcomeCall);
    assertEquals(response, expectedResponse);
  }

  @Test
  void shouldGetNotProcessedWelcomeCalls() throws Exception {
    // Given
    var givenWelcomeCalls = List.of(
        buildWelcomeCall(1L, "policyRef1", PENDING, null),
        buildWelcomeCall(2L, "policyRef2", PENDING, null));
    var expectedResponse = objectMapper.writeValueAsString(mapper.toApiWelcomeCallResponseList(givenWelcomeCalls));
    when(welcomeCallService.getNotAnsweredWelcomeCalls()).thenReturn(givenWelcomeCalls);

    // When
    String response = mockMvc
        .perform(get(BASE_URL+"/notanswered")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn()
        .getResponse().getContentAsString();

    // Then
    verify(welcomeCallService).getNotAnsweredWelcomeCalls();
    assertEquals(response, expectedResponse);
  }
}