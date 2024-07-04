package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void authenticate(String username, String plainTextPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials."));

        boolean userMatch = passwordEncoder.matches(plainTextPassword, user.getPassword());

        if (!userMatch) {
            throw new ServiceException("Invalid credentials.");
        }

    }
}

