package com.example.belajarspringboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class PermittedPathsConfig {

    @Bean
    public Set<String> permittedPaths() {
        Set<String> permittedPaths = new HashSet<>();
        permittedPaths.add("/api/v1/register");
        permittedPaths.add("/api/v1/login");
        permittedPaths.add("/api/v1/product/");
        permittedPaths.add("/api/v1/product/{id}");
        return permittedPaths;
    }
}