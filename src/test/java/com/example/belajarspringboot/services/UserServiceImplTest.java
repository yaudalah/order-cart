package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.repositories.UserRepository;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterSuccess() {
        // Given
        User user = new User();
        user.setUsername("new-user");
        user.setPassword("password");
        user.setEmail("newuser@example.com");
        user.setPhoneNumber("1234567890");

        when(userRepository.findByUsername("new-user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        SuccessApiResponse<Object> response = userService.register(user);

        // Then
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("Registration successful.", response.getMessage());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        // Given
        User existingUser = User.builder()
                .username("existinguser")
                .password("password")
                .email("existinguser@example.com")
                .phoneNumber("1234567890")
                .build();

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        User newUser = new User();
        newUser.setUsername("existinguser");
        newUser.setPassword("password");
        newUser.setEmail("newuser@example.com");
        newUser.setPhoneNumber("1234567890");

        // When & Then
        assertThrows(ServiceException.class, () -> userService.register(newUser));
    }

}