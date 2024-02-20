package org.insurance.welcomeservice.api.model.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.insurance.welcomeservice.api.model.ApiWelcomeCallStatus;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class ApiWelcomeCallResponse {

  private String policyReference;
  private String telephone;
  private String email;
  private LocalDateTime policyIssuedAt;
  private ApiWelcomeCallStatus status;
  private String agentId;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  private LocalDateTime createdAt;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  private LocalDateTime updatedAt;
}
