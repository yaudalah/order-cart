package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.dto.SuccessApiResponse;
import com.example.belajarspringboot.models.dto.UserLoginReqDTO;
import com.example.belajarspringboot.models.dto.UserResDTO;
import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.repositories.UserRepository;
import com.example.belajarspringboot.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

    public SuccessApiResponse<Object> login(UserLoginReqDTO user) {
        log.info("User {} try logged in.", user.getUsername());
        authenticationService.authenticate(user.getUsername(), user.getPassword());

        final UserDetails userDetails = loadUserByUsername(user.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        final UserResDTO buildData = UserResDTO.builder()
                .token(jwt)
                .username(user.getUsername())
                .build();

        return SuccessApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success Login")
                .data(buildData)
                .build();
    }

    public User getUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
