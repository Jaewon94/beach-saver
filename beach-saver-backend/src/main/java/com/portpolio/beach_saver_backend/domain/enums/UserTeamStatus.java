package com.portpolio.beach_saver_backend.domain.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum UserTeamStatus implements CodeEnum {
  ACTIVE("ACTIVE", "팀 소속 중"),
  LEFT("LEFT", "팀 탈퇴");

  private final String code;
  private final String description;

  UserTeamStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  private static final Map<String, UserTeamStatus> CODE_MAP = new HashMap<>();

  static {
    for (UserTeamStatus value : values()) {
      CODE_MAP.put(value.code, value);
    }
  }

  public static UserTeamStatus fromCode(String code) {
    return CODE_MAP.get(code);
  }
}
