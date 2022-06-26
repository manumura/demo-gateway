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
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private Boolean enabled;

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
      String uuid, String firstName, String lastName, String email, String phoneNumber, Boolean enabled) {
    super(username, password, authorities);
    this.uuid = uuid;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "UserData{" +
        "uuid='" + uuid + '\'' +
        ", username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", enabled=" + enabled +
        '}';
  }
}
