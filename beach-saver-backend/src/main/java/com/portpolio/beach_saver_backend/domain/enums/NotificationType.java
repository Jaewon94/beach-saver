package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/** 알림 유형 코드 */
@Getter
public enum NotificationType {
    PUSH("PUSH", "푸시 알림"),
    SMS("SMS", "문자"),
    EMAIL("EMAIL", "이메일");

    private final String code;
    private final String description;

    NotificationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<String, NotificationType> CODE_MAP = new HashMap<>();
    static {
        for (NotificationType value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }
    public static NotificationType fromCode(String code) {
        return CODE_MAP.get(code);
    }
} 