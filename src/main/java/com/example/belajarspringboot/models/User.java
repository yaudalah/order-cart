package com.example.belajarspringboot.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_user")
@Builder
public class User extends BaseModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "user_id", updatable = false, nullable = false, length = 36)
    private UUID userId;

    @Column(name = "username", length = 25, unique = true, nullable = false)
    @NotBlank(message = "The username must not be empty.")
    private String username;

    @Column(unique = true, nullable = false, length = 25)
    @Email
    @Size(min = 5, message = "Email minimum has to be greater than 5 characters.")
    private String email;

    @Column(name = "phone_number", unique = true, length = 13, nullable = false)
    @Size(min = 11, max = 13, message = "Phone Number minimum has to be greater than 11 and maximum 13 characters.")
    @NotNull(message = "Phone Number Must is Mandatory")
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "join_date", nullable = false)
    @JsonIgnore
    private Instant joinDate;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 8, message = "Password minimum has to be greater than 8 characters.")
    private String password;

    @Override
    protected void onCreate() {
        super.onCreate();
        joinDate = Instant.now();
    }
}
