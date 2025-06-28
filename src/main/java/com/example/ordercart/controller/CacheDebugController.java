package com.example.ordercart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Profile("dev")
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheDebugController {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/{cacheName}")
    public ResponseEntity<?> listKeys(@PathVariable String cacheName) {
        Set<String> keys = redisTemplate.keys(cacheName + "*");
        return ResponseEntity.ok(keys);
    }

    @GetMapping("/{cacheName}/entry")
    public ResponseEntity<?> getEntry(@PathVariable String cacheName, @RequestParam String key) {
        String fullKey = cacheName + "::" + key;
        Object value = redisTemplate.opsForValue().get(fullKey);
        return ResponseEntity.ok(value);
    }
}
