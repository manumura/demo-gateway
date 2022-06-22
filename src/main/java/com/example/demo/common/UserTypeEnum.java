package com.example.demo.common;

import lombok.Getter;

@Getter
public enum UserTypeEnum {

  DRIVER(1, "DRIVER", "driver"),
  PORTAL(2, "PORTAL", "portalUser");

  private final Integer id;

  private final String code;

  private final String clientId;

  UserTypeEnum(Integer id, String code, String clientId) {
    this.id = id;
    this.code = code;
    this.clientId = clientId;
  }

}
