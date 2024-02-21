package org.insurance.welcomeservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The welcome call entity that describes the `welcome_calls` table.
 * We do not use lombok library as it does not work well with JPA entities.
 */
@Getter
@Setter
@Entity
@Table(name = "welcome_calls")
public final class WelcomeCall {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "policy_reference", unique = true)
  private String policyReference;

  @Column(name = "telephone")
  private String telephone;

  @Column(name = "email")
  private String email;

  @Column(name = "policy_issued_at")
  private LocalDateTime policyIssuedAt;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private WelcomeCallStatus status;

  @Column(name = "agent_id")
  private String agentId;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WelcomeCall that = (WelcomeCall) o;
    return Objects.equals(id, that.id) && Objects.equals(policyReference,
        that.policyReference) && Objects.equals(telephone, that.telephone)
        && Objects.equals(email, that.email) && Objects.equals(policyIssuedAt,
        that.policyIssuedAt) && status == that.status && Objects.equals(agentId,
        that.agentId) && Objects.equals(createdAt, that.createdAt)
        && Objects.equals(updatedAt, that.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, policyReference, telephone, email, policyIssuedAt, status, agentId,
        createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "WelcomeCall{" +
        "id=" + id +
        ", policyReference='" + policyReference + '\'' +
        ", telephone='" + telephone + '\'' +
        ", email='" + email + '\'' +
        ", policyIssuedAt=" + policyIssuedAt +
        ", status=" + status +
        ", agentId='" + agentId + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }

  public static WelcomeCallBuilder builder() {
    return new WelcomeCallBuilder();
  }

  public static final class WelcomeCallBuilder {
    private final WelcomeCall welcomeCall;

    private WelcomeCallBuilder() {
      welcomeCall = new WelcomeCall();
    }

    public WelcomeCallBuilder id(Long id) {
      welcomeCall.setId(id);
      return this;
    }

    public WelcomeCallBuilder policyReference(String policyReference) {
      welcomeCall.setPolicyReference(policyReference);
      return this;
    }

    public WelcomeCallBuilder telephone(String telephone) {
      welcomeCall.setTelephone(telephone);
      return this;
    }

    public WelcomeCallBuilder email(String email) {
      welcomeCall.setEmail(email);
      return this;
    }

    public WelcomeCallBuilder policyIssuedAt(LocalDateTime policyIssuedAt) {
      welcomeCall.setPolicyIssuedAt(policyIssuedAt);
      return this;
    }

    public WelcomeCallBuilder status(WelcomeCallStatus status) {
      welcomeCall.setStatus(status);
      return this;
    }

    public WelcomeCallBuilder agentId(String agentId) {
      welcomeCall.setAgentId(agentId);
      return this;
    }

    public WelcomeCallBuilder createdAt(LocalDateTime createdAt) {
      welcomeCall.setCreatedAt(createdAt);
      return this;
    }

    public WelcomeCallBuilder updatedAt(LocalDateTime updatedAt) {
      welcomeCall.setUpdatedAt(updatedAt);
      return this;
    }

    public WelcomeCall build() {
      return welcomeCall;
    }
  }
}


