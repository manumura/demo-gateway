package com.example.demo.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class UserData extends User {

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

  private boolean geoAccepted;

  public UserData() {
    super(" ", "", Collections.emptyList());
  }

  @Override
  @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
  public Collection<GrantedAuthority> getAuthorities() {
    return super.getAuthorities();
  }

  // TODO
//  @Builder
  public UserData(String username, String password, Collection<? extends GrantedAuthority> authorities,
      String uuid, String driverUuid, String firstName, String lastName, String email, String phoneNumber, Boolean enabled, List<ProviderData> providers, String buCode, UserTypeEnum userType, AuthorizationLevel authorizationLevel, boolean geoAccepted) {
    super(username, password, authorities);
    this.uuid = uuid;

    this.driverUuid = driverUuid;

    this.username = username;

    this.firstName = firstName;

    this.lastName = lastName;

    this.email = email;

    this.phoneNumber = phoneNumber;

    this.enabled = enabled;

    this.providers = providers;

    this.buCode = buCode;

    this.userType = userType;

    this.authorizationLevel = authorizationLevel;

    this.geoAccepted = geoAccepted;
  }

  public List<String> getProviderIds() {
    return this.providers == null ? Collections.emptyList() : this.providers.stream().map(ProviderData::getProviderId).collect(
        Collectors.toList());
  }

  public List<String> getRecipients() {
    final List<String> roles = new ArrayList<>();
    if (UserTypeEnum.PORTAL.equals(userType)) {
      roles.add(UserRecipientEnum.PORTAL_RECIPIENT.getRecipientName());
    } else {
      if (getAuthorities() != null) {
        for (GrantedAuthority authority : getAuthorities()) {
          if (UserRecipientEnum.DRIVER_RECIPIENT.getRecipientName().equalsIgnoreCase(authority.getAuthority())) {
            roles.add(UserRecipientEnum.DRIVER_RECIPIENT.getRecipientName());
          } else if (UserRecipientEnum.MOBILE_DISPATCHER_RECIPIENT.getRecipientName().equalsIgnoreCase(authority.getAuthority())) {
            roles.add(UserRecipientEnum.MOBILE_DISPATCHER_RECIPIENT.getRecipientName());
          }
        }
      }
    }
    return roles;
  }

  public boolean hasProviderId(String providerId) {
    return getProviderIds().contains(providerId);
  }

  public boolean hasAnyProviderIds(List<String> providerIds) {
    return CollectionUtils.containsAny(getProviderIds(), providerIds);
  }

  @Override
  public String toString() {
    return "UserData{" +
        "uuid='" + uuid + '\'' +
        ", driverUuid='" + driverUuid + '\'' +
        ", username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", enabled=" + enabled +
        ", providers=" + providers +
        ", buCode='" + buCode + '\'' +
        ", userType=" + userType +
        ", authorizationLevel=" + authorizationLevel +
        ", geoAccepted=" + geoAccepted +
        '}';
  }
}
