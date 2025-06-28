package com.example.ordercart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class CacheVersionService {
    private final Map<String, AtomicLong> userVersions = new ConcurrentHashMap<>();

    public long getVersion(String username) {
        log.info("[CacheVersionService] Get version for username {}", username);
        return userVersions.computeIfAbsent(username, k -> new AtomicLong(0)).get();
    }

    public void incrementVersion(String username) {
        log.info("[CacheVersionService] incrementVersion username:{}", username);
        userVersions.computeIfAbsent(username, k -> new AtomicLong(0)).incrementAndGet();
    }
}
