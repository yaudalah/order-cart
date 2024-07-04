package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import com.example.belajarspringboot.models.DTO.UserLoginReqDTO;
import com.example.belajarspringboot.models.DTO.UserResDTO;
import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.repositories.UserRepository;
import com.example.belajarspringboot.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("Invalid credentials."));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public SuccessApiResponse<Object> register(User req) {
        userRepository.findByUsername(req.getUsername()).ifPresent(existingUser -> {
            throw new ServiceException(String.format("User %s already registered.", existingUser.getUsername()));
        });

        final String hashedPassword = passwordEncoder.encode(req.getPassword());
        User user = User.builder()
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .username(req.getUsername())
                .password(hashedPassword)
                .build();

        userRepository.save(user);

        log.info("User {} registered.", user.getUsername());

        return SuccessApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Registration successful.")
                .build();
    }

    public UserResDTO login(String loginId, UserLoginReqDTO user) {
        log.info("login id: {}", loginId);
        log.info("User try {} logged in.", user.getUsername());
        authenticationService.authenticate(user.getUsername(), user.getPassword());

        final UserDetails userDetails = loadUserByUsername(user.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return UserResDTO.builder()
                .token(jwt)
                .username(user.getUsername())
                .build();
    }
}
