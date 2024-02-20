package org.insurance.welcomeservice.repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.insurance.welcomeservice.model.WelcomeCall;
import org.insurance.welcomeservice.model.WelcomeCallStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WelcomeCallRepository extends JpaRepository<WelcomeCall, Long> {

  /**
   * Finds a welcome call by policyReference.
   *
   * @param policyReference the policyReference.
   * @return an optional containing the welcome call or empty if it is not found.
   */
  Optional<WelcomeCall> findByPolicyReference(String policyReference);

  /**
   * Returns a list of welcome calls by status.
   *
   * @param status the status.
   * @return a list of welcome calls with the specified status.
   */
  List<WelcomeCall> findByStatus(WelcomeCallStatus status);


  /**
   * Returns all welcome calls that have the given status, and the policyIssuedAt is before the
   * given date. Because this method is called from the scheduler to find and update the delayed
   * welcome calls, we apply a PESSIMISTIC_WRITE lock.
   *
   * @return a list of welcome calls with a given status and with policyIssuedAt before the givenDate.
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select wc from WelcomeCall wc where wc.status = :status and wc.policyIssuedAt <= :givenDate")
  List<WelcomeCall> findByStatusAndPolicyIssuedAtBefore(WelcomeCallStatus status,
      LocalDateTime givenDate);

  /**
   * Returns all welcome calls that have the given status, and the policyIssuedAt is after the
   * given date.
   *
   * @return a list of welcome calls with a given status and with policyIssuedAt after the givenDate.
   */
  @Query("select wc from WelcomeCall wc where wc.status = :status and wc.policyIssuedAt >= :givenDate")
  List<WelcomeCall> findByStatusAndPolicyIssuedAtAfter(WelcomeCallStatus status,
      LocalDateTime givenDate);
}
