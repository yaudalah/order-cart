package com.example.ordercart.model.converter;

import com.example.ordercart.common.enums.IsLockedEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IsLockedEnumConverter implements AttributeConverter<IsLockedEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(IsLockedEnum attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public IsLockedEnum convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : IsLockedEnum.fromValue(dbData);
    }
}
