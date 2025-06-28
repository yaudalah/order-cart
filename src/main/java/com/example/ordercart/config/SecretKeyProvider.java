package com.example.ordercart.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Component
public class SecretKeyProvider {

    @Value("${secret.key.path}")
    private String keyPath;
    private String secretKey;

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(keyPath);
        List<String> lines = Files.readAllLines(path);

        // Clean header/footer and join
        this.secretKey = lines.stream()
                .filter(line -> !line.startsWith("-----"))
                .collect(Collectors.joining());
    }

}
