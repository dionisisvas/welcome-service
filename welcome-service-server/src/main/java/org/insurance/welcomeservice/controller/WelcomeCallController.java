package org.insurance.welcomeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.insurance.welcomeservice.api.model.request.ApiWelcomeCallRequest;
import org.insurance.welcomeservice.api.model.response.ApiWelcomeCallResponse;
import org.insurance.welcomeservice.mappers.WelcomeCallApiMapper;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.service.WelcomeCallService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Welcome Calls API",
    description = "Handle welcome calls (retrieve pending calls, update status and more.")
@RequestMapping(value = "v1/welcomecalls")
public class WelcomeCallController {
  private final WelcomeCallService welcomeCallService;

  private final WelcomeCallApiMapper mapper;

  public WelcomeCallController(WelcomeCallService welcomeCallService,
      WelcomeCallApiMapper welcomeCallApiMapper) {
    this.welcomeCallService = welcomeCallService;
    this.mapper = welcomeCallApiMapper;
  }

  @Operation(
      summary = "List all not pending welcome calls be chronological order.",
      description = "Lists all pending welcome calls by chronological order (oldest first)."
          + " Pending calls are those that have status = PENDING and are not assigned to an agent."
  )
  @GetMapping("/pending")
  @ResponseStatus(value = HttpStatus.OK)
  public List<ApiWelcomeCallResponse> getPendingWelcomeCalls() {
    return mapper.toApiWelcomeCallResponseList(welcomeCallService.getPendingWelcomeCalls());
  }

  @Operation(
      summary = "Updates a welcome call entry in the provided request body.",
      description = "Updates a welcome call entry in the provided request body. The policy reference"
          + " will be used to find the entry and update it."
  )
  @PatchMapping("/welcomecall")
  @ResponseStatus(value = HttpStatus.OK)
  public ApiWelcomeCallResponse updatePolicyReference(
      @RequestBody ApiWelcomeCallRequest request) {
    WelcomeCall welcomeCall = mapper.toWelcomeCallModel(request);
    return mapper.toApiWelcomeCallResponse(welcomeCallService.updateWelcomeCall(welcomeCall));
  }

  @Operation(
      summary = "List all not answered welcome calls by chronological order, which are not delayed.",
      description = "Lists all not answered welcome calls by chronological order (oldest first)."
          + " It will return the welcome calls that have status = NO_ANSWER and two days have"
          + " not passed since policy was issued. Used in case an agent wishes to retry calling a client."
  )
  @GetMapping("/notanswered")
  @ResponseStatus(value = HttpStatus.OK)
  public List<ApiWelcomeCallResponse> getNotAnsweredWelcomeCalls() {
    return mapper.toApiWelcomeCallResponseList(welcomeCallService.getNotAnsweredWelcomeCalls());
  }
}
