package com.example.demo.oauth2;

import com.example.demo.common.AuthenticatedUserData;
import com.example.demo.common.AuthorizationLevel;
import com.example.demo.common.GrantedAuthorityDeserializer;
import com.example.demo.common.ProviderData;
import com.example.demo.common.UserTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
@JsonInclude(Include.NON_NULL)
public class Oauth2UserData implements OAuth2User {

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
  @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
  private List<GrantedAuthority> authorities;
  private boolean geoAccepted;

  public Oauth2UserData(AuthenticatedUserData authenticatedUserData) {
    this.uuid = authenticatedUserData.getUuid();
    this.driverUuid = authenticatedUserData.getDriverUuid();
    this.username = authenticatedUserData.getUsername();
    this.firstName = authenticatedUserData.getFirstName();
    this.lastName = authenticatedUserData.getLastName();
    this.email = authenticatedUserData.getEmail();
    this.phoneNumber = authenticatedUserData.getPhoneNumber();
    this.enabled = authenticatedUserData.getEnabled();
    this.providers = authenticatedUserData.getProviders();
    this.buCode = authenticatedUserData.getBuCode();
    this.userType = authenticatedUserData.getUserType();
    this.authorizationLevel = authenticatedUserData.getAuthorizationLevel();
    this.geoAccepted = authenticatedUserData.isGeoAccepted();

    this.authorities  = new ArrayList<>();
    for (String authority : authenticatedUserData.getAuthorities()) {
      authorities.add(new SimpleGrantedAuthority(authority));
    }
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Collections.emptyMap();
  }

  @Override
  public String getName() {
    return username;
  }
}