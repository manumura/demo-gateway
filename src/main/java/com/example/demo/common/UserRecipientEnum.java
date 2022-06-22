package com.example.demo.common;

import lombok.Getter;

@Getter
public enum UserRecipientEnum {

  DRIVER_RECIPIENT(1, "DRIVER"),
  PORTAL_RECIPIENT(2, "PORTAL"),
  MOBILE_DISPATCHER_RECIPIENT(3, "MOBILEDISPATCHER");

  private final Integer id;

  private final String recipientName;

  UserRecipientEnum(Integer id, String recipientName) {
    this.id = id;
    this.recipientName = recipientName;
  }

}
