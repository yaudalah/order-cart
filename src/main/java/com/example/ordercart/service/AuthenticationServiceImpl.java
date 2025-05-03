package com.example.ordercart.service;

import com.example.ordercart.entity.User;
import com.example.ordercart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void authenticate(String username, String password) {
        log.info("[AuthenticationServiceImpl] Authenticating user {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("Invalid username or password"));

        final boolean matchesPassword = passwordEncoder.matches(password, user.getPassword());
        if (!matchesPassword) {
            throw new ServiceException("Invalid username or password");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("Invalid credentials."));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

}