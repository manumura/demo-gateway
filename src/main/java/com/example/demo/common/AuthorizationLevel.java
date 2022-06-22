package com.example.demo.common;

import lombok.Getter;

@Getter
public enum AuthorizationLevel {

  ALL(1, "ALL"),
  PROVIDER(2, "PROVIDER");

  private final int id;

  private final String code;

  AuthorizationLevel(int id, String code) {
    this.id = id;
    this.code = code;
  }
}
