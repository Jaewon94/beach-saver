package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/** 이미지 유형 코드 */
@Getter
public enum ImageType {
    INVESTIGATION_MAIN("INVESTIGATION_MAIN", "조사 메인 이미지"),
    INVESTIGATION_AREA("INVESTIGATION_AREA", "조사 구역 이미지"),
    CLEANING_BEFORE_AREA("CLEANING_BEFORE_AREA", "청소 전 구역 이미지"),
    CLEANING_AFTER_AREA("CLEANING_AFTER_AREA", "청소 후 구역 이미지"),
    COLLECTION_DEPOT("COLLECTION_DEPOT", "임시 집하장(수거용) 이미지"),
    COLLECTION_COMPLETED("COLLECTION_COMPLETED", "수거 완료 이미지");

    private final String code;
    private final String description;

    ImageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<String, ImageType> CODE_MAP = new HashMap<>();
    static {
        for (ImageType value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }
    public static ImageType fromCode(String code) {
        return CODE_MAP.get(code);
    }
} 