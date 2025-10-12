package com.example.ordercart.common.enums;

import lombok.Getter;

@Getter
public enum IsLockedEnum {
    LOCKED(0),
    UNLOCKED(-1);

    private final int value;

    IsLockedEnum(int value) {
        this.value = value;
    }

    public static IsLockedEnum fromValue(int v) {
        for (IsLockedEnum e : values()) if (e.value == v) return e;
        throw new IllegalArgumentException("Unknown IsLockedEnum value: " + v);
    }
}
