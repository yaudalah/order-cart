package com.example.belajarspringboot.repositories;

import com.example.belajarspringboot.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {
    Optional<User> findByUsername(String userName);
    User findUserByUsername(String userName);
}
