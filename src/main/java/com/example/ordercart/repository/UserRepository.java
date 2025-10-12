package com.example.ordercart.repository;

import com.example.ordercart.entity.User;
import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String userName);

    @QueryHints(@QueryHint(name = "jakarta.persistence.fetchSize", value = "5000"))
    @Query(value = "SELECT u FROM User u")
    Stream<User> streamAllUsers();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.retry = u.retry + 1, " +
            "u.isLocked = CASE WHEN (u.retry + 1) >= 3 THEN com.example.ordercart.common.enums.IsLockedEnum.LOCKED ELSE u.isLocked END " +
            "WHERE u.id = :id")
    void incrementRetryAndLockIfNeeded(@Param("id") UUID id);
}
