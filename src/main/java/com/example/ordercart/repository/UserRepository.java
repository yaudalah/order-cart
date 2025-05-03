package com.example.ordercart.repository;

import com.example.ordercart.entity.User;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
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
}
