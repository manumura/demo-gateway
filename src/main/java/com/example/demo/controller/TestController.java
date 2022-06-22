package com.example.demo.controller;

import com.example.demo.common.UserData;
import com.example.demo.dto.Topic;
import com.example.demo.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

  private final TopicService topicService;

  @GetMapping("/config/topics")
  public ResponseEntity<List<Topic>> getTopics() {
    final List<Topic> topics = topicService.getTopicsFromCache();
    return ResponseEntity.ok(topics);
  }

  // TODO test
  @GetMapping("/test")
  public ResponseEntity<UserData> test(Principal principal) {

    System.out.println("authenticated user: " + getUserFromSecurityContext());

    System.out.println("principal: " + principal);

    UserData user = getUserDataFromPrincipal(principal);
    System.out.println("user: " + user);

    return ResponseEntity.ok(user);
  }

  @GetMapping("/test2")
  public Mono<UserData> test2() {

    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(UserData.class)
        .doOnNext(u -> {
          System.out.println("u " + u);
        });
  }

  private UserData getUserDataFromPrincipal(Principal principal) {
    UserData user = null;
    if (principal instanceof UsernamePasswordAuthenticationToken) {
      UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;

      if (token.getPrincipal() instanceof UserData) {
        user = (UserData) token.getPrincipal();
      }
    }
    return user;
  }

  private Object getUserFromSecurityContext() {
    if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication() == null) {
      System.err.println("authentication null");
      return null;
    }

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null) {
      System.err.println("principal null " + authentication);
      return null;
    }

    Object user = authentication.getPrincipal();
    return user;
  }
}
