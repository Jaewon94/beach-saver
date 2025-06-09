package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * 사용자 상태 Enum
 */
@Getter
public enum UserStatus implements CodeEnum {
    ACTIVE("ACTIVE", "활성"),
    SUSPENDED("SUSPENDED", "정지"),
    WITHDRAWN("WITHDRAWN", "탈퇴");

    private final String code;
    private final String description;

    UserStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<String, UserStatus> CODE_MAP = new HashMap<>();
    static {
        for (UserStatus value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }
    public static UserStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
}