package com.eden.edenbe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByParent(int parent);

    @Query("SELECT u FROM User u WHERE u.direct_referral = :directReferral")
    List<User> findByDirectReferral(@Param("directReferral") Long direct_referral);
}
