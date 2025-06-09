package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/** 집하장(임시 집하장) 상태 코드 */
@Getter
public enum CollectionDepotStatus {
    WAITING("WAITING", "수거대기"),
    COMPLETED("COMPLETED", "수거완료");

    private final String code;
    private final String description;

    CollectionDepotStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<String, CollectionDepotStatus> CODE_MAP = new HashMap<>();
    static {
        for (CollectionDepotStatus value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }
    public static CollectionDepotStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
} 