package org.insurance.welcomeservice.mappers;

import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.rabbitmq.PolicyIssuedEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Contains methods to convert PolicyIssuedEventDto objects to domain WelcomeCall objects.
 */
@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WelcomeCallDtoMapper {

  @Mapping(target = "policyIssuedAt", source = "policyIssuedTimestamp")
  WelcomeCall toWelcomeCall(PolicyIssuedEventDto policyIssuedEventDto);
}
