package com.example.ordercart.service;

import com.example.ordercart.entity.User;
import com.example.ordercart.exception.ServiceException;
import com.example.ordercart.model.dto.UserDto;
import com.example.ordercart.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvExportService {

    private final UserRepository userRepository;

    private static final Map<String, Method> COLUMN_GETTERS = new HashMap<>();

    @PostConstruct
    private static void initColumnMappings() {
        Arrays.stream(User.class.getDeclaredFields())
                .forEach(field -> {
                    String fieldName = field.getName();
                    String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        Method getter = User.class.getMethod(getterName);
                        COLUMN_GETTERS.put(fieldName, getter);
                    } catch (NoSuchMethodException e) {
                        log.warn("No getter found for field: {}", fieldName);
                    }
                });
    }

    @Transactional(readOnly = true)
    public void exportEmployeesToCsv(OutputStream outputStream, UserDto userDTO) {
        try (CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream),
                CSVFormat.DEFAULT.builder().setHeader(userDTO.getExportedColumn().toArray(new String[0])).get())) {
            log.info("Start export..");
            try (var userStream = userRepository.streamAllUsers()) {
                userStream.forEach(user -> {
                    try {
                        csvPrinter.printRecord(mapUserToColumns(user, userDTO.getExportedColumn()));
                    } catch (IOException e) {
                        throw new ServiceException("Error writing record: " + e.getMessage());
                    }
                });
            }
            log.info("Done export..");

        } catch (IOException e) {
            throw new ServiceException("Failed to generate CSV: " + e.getMessage());
        }
    }

    private Object[] mapUserToColumns(User user, List<String> columns) {
        return columns.stream()
                .map(col -> {
                    Method getter = COLUMN_GETTERS.get(col);
                    if (getter == null) {
                        throw new ServiceException("Invalid column: " + col);
                    }
                    try {
                        Object value = getter.invoke(user);
                        return value != null ? value : "";
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ServiceException("Error accessing field: " + col, e);
                    }
                })
                .toArray();
    }
}