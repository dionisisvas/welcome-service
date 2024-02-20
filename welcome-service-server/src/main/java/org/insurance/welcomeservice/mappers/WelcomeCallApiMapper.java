package org.insurance.welcomeservice.mappers;

import java.util.List;
import org.insurance.welcomeservice.api.model.request.ApiWelcomeCallRequest;
import org.insurance.welcomeservice.api.model.response.ApiWelcomeCallResponse;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.mapstruct.Mapper;

/**
 * Contains methods to convert WelcomeCall API objects to domain models and vice versa.
 */
@Mapper(componentModel = "spring")
public interface WelcomeCallApiMapper {

  ApiWelcomeCallResponse toApiWelcomeCallResponse(WelcomeCall welcomeCall);

  List<ApiWelcomeCallResponse> toApiWelcomeCallResponseList(List<WelcomeCall> welcomeCalls);

  WelcomeCall toWelcomeCallModel(ApiWelcomeCallRequest apiWelcomeCallRequest);
}
