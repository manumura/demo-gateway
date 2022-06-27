package com.example.demo.config;

//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//
//@RequiredArgsConstructor
//@Configuration
//@EnableRedisRepositories
//public class RedisConfig {
//
//  private final LettuceConnectionFactory redisConnectionFactory;
//
//  @Bean
//  public RedisTemplate<String, Object> redisTemplate() {
//    final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//    redisTemplate.setConnectionFactory(redisConnectionFactory);
//    return redisTemplate;
//  }
//
//}
