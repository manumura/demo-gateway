package com.example.demo.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AuthenticatedUserData {

  private String uuid;
  private String driverUuid;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private Boolean enabled;
  private List<ProviderData> providers;
  private String buCode;
  private UserTypeEnum userType;
  private AuthorizationLevel authorizationLevel;
  private List<String> authorities;
  private boolean geoAccepted;
}