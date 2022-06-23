package com.example.demo.config;

import com.example.demo.dto.Topic;
import com.example.demo.service.TopicService;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class RouteConfig {

  private static final String TOPIC_HEADER_NAME = "topic";

  @Value("${client.demo-gateway-client1.url}")
  private String SERVICE1_URL;

  @Value("${client.demo-gateway-client2.url}")
  private String SERVICE2_URL;

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder, TopicService topicService) {

    List<Topic> topics = new ArrayList<>();

    List<String> services = List.of(SERVICE1_URL, SERVICE2_URL);
    for (String service: services) {
      try {
        List<Topic> t = topicService.getTopicsByService(service);
        if (t != null) {
          t.forEach(topic -> {
            topic.setService(service);
          });
          log.debug("client {} topics: {}", service, t);
          topics.addAll(t);
        }
      } catch (Exception e) {
        log.error("Cannot get topics from " + service);
      }
    }

    if (CollectionUtils.isEmpty(topics)) {
      throw new RuntimeException("No topics found, cannot build routes !!");
    }

    RouteLocatorBuilder.Builder routeLocatorBuilder = builder.routes();

    for (Topic topic : topics) {
      log.debug("Creating route: {} {}", topic.getCode(), topic.getService());
      routeLocatorBuilder.route(r -> r.path("/config/**")
          .and().header(TOPIC_HEADER_NAME, topic.getCode())
          .uri(topic.getService()));
    }

    try {
      topicService.addTopicsToCache(topics);
    } catch (Exception e) {
      log.error("Could not add topics to cache", e);
    }

    return routeLocatorBuilder.build();
  }

//  @Bean
//  public HttpClient httpClient() {
//    return HttpClient.create().wiretap("LoggingFilter", LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
//  }
}
