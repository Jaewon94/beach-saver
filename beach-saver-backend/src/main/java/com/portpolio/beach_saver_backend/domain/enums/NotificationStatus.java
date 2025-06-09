package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/** 알림 상태 코드 */
@Getter
public enum NotificationStatus implements CodeEnum {
    UNREAD("UNREAD", "안읽음"),
    READ("READ", "읽음");

    private final String code;
    private final String description;

    NotificationStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<String, NotificationStatus> CODE_MAP = new HashMap<>();
    static {
        for (NotificationStatus value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }
    public static NotificationStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
} 