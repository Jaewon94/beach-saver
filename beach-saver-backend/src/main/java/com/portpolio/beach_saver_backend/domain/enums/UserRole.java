package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * 사용자 역할 Enum
 */
@Getter
public enum UserRole implements CodeEnum {
    PLATFORM_ADMIN("PLATFORM_ADMIN", "플랫폼 전체 관리자"),
    CITY_ADMIN("CITY_ADMIN", "시/도 관리자"),
    DISTRICT_ADMIN("DISTRICT_ADMIN", "구/군 관리자"),
    INVESTIGATOR("INVESTIGATOR", "조사자"),
    CLEANER("CLEANER", "청소자"),
    COLLECTOR("COLLECTOR", "수거자"),
    CITIZEN("CITIZEN", "시민 자원봉사자");

    private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // code → Enum 역변환
    private static final Map<String, UserRole> CODE_MAP = new HashMap<>();
    static {
        for (UserRole value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }
    public static UserRole fromCode(String code) {
        return CODE_MAP.get(code);
    }
}