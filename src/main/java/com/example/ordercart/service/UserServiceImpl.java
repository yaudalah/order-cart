package com.example.ordercart.service;

import com.example.ordercart.entity.User;
import com.example.ordercart.model.request.UserLoginReq;
import com.example.ordercart.model.response.UserRes;
import com.example.ordercart.repository.UserRepository;
import com.example.ordercart.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final com.example.ordercart.service.AuthenticationServiceImpl authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(User req) {
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

        log.info("[UserServiceImpl] Saving user: {}", user);
        userRepository.save(user);

        log.info("User {} registered.", user.getUsername());
    }

    public UserRes login(UserLoginReq user) {
        authenticationService.authenticate(user.getUsername(), user.getPassword());

        final UserDetails userDetails = buildUserDetails(user.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return UserRes.builder()
                .token(jwt)
                .username(user.getUsername())
                .build();
    }

    public UserDetails buildUserDetails(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found.", username)));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
