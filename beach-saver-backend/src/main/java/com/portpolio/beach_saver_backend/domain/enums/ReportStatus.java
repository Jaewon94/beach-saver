package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/** 보고서 상태 코드 */
@Getter
public enum ReportStatus implements CodeEnum {
    DRAFT("DRAFT", "임시"),
    SUBMITTED("SUBMITTED", "제출"),
    APPROVED("APPROVED", "승인"),
    REJECTED("REJECTED", "반려");

    private final String code;
    private final String description;

    ReportStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<String, ReportStatus> CODE_MAP = new HashMap<>();
    static {
        for (ReportStatus value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }
    public static ReportStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
}