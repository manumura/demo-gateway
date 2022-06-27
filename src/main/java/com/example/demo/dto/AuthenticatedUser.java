package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class AuthenticatedUser {

  private String uuid;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private Boolean enabled;
  private List<String> authorities;
}