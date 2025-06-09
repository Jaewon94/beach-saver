package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/** 보고서 유형 코드 */
@Getter
public enum ReportType implements CodeEnum {
    INVESTIGATION("INVESTIGATION", "조사 보고서"),
    CLEANING("CLEANING", "청소 보고서"),
    COLLECTION_REQUEST("COLLECTION_REQUEST", "수거용(요청) 보고서(청소자 작성)");

    private final String code;
    private final String description;

    ReportType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<String, ReportType> CODE_MAP = new HashMap<>();
    static {
        for (ReportType value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }
    public static ReportType fromCode(String code) {
        return CODE_MAP.get(code);
    }
} 