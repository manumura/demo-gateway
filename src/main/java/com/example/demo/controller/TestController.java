package com.example.demo.controller;

import com.example.demo.dto.User;
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

  @GetMapping("/admin/config/topics")
  public ResponseEntity<List<Topic>> getTopics() {
    final List<Topic> topics = topicService.getTopicsFromCache();
    return ResponseEntity.ok(topics);
  }

  @GetMapping("/super-admin/message")
  public ResponseEntity<String> getProtectedMessage() {
    return ResponseEntity.ok("super admin endpoint reachable");
  }

  @GetMapping("/public/message")
  public ResponseEntity<String> getMessage() {
    return ResponseEntity.ok("public endpoint reachable");
  }

  // TODO test
  @GetMapping("/user")
  public ResponseEntity<User> user(Principal principal) {

    System.out.println("authenticated user: " + getUserFromSecurityContext());

    System.out.println("principal: " + principal);

    User user = getUserDataFromPrincipal(principal);
    System.out.println("user: " + user);

    return ResponseEntity.ok(user);
  }

  @GetMapping("/user2")
  public Mono<User> user2() {

    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .cast(User.class)
        .doOnNext(user -> {
          System.out.println("user2 " + user);
        });
  }

  private User getUserDataFromPrincipal(Principal principal) {
    User user = null;
    if (principal instanceof UsernamePasswordAuthenticationToken) {
      UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;

      if (token.getPrincipal() instanceof User) {
        user = (User) token.getPrincipal();
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
