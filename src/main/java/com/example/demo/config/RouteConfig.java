package com.example.demo.config;

import com.example.demo.properties.Client;
import com.example.demo.dto.Topic;
import com.example.demo.properties.ApplicationProperties;
import com.example.demo.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class RouteConfig {

  private static final String TOPIC_HEADER_NAME = "topic";

  private final ApplicationProperties applicationProperties;

  public RouteConfig(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder, TopicService topicService) {

    if (applicationProperties == null || CollectionUtils.isEmpty(applicationProperties.getClients())) {
      log.error("clients properties not found");
      return builder.routes().build();
    }

    RouteLocatorBuilder.Builder routeLocatorBuilder = builder.routes();

    List<Topic> topics = new ArrayList<>();
    for (Client client : applicationProperties.getClients()) {
      // Path-based route
      log.debug("Creating route: path {} -> {}", client.getName(), client.getUrl());
      routeLocatorBuilder.route(r -> r.path("/" + client.getName() + "/**")
              // strip prefix
//              .filters(f -> f.stripPrefix(1))
              .filters(f -> f.rewritePath("^/" + client.getName(), ""))
              .uri(client.getUrl()));

      try {
        List<Topic> t = topicService.getTopicsByService(client.getUrl());
        if (t != null) {
          t.forEach(topic -> {
            topic.setService(client.getUrl());
          });
          log.debug("client {} topics: {}", client.getUrl(), t);
          topics.addAll(t);
        }
      } catch (Exception e) {
        log.error("Cannot get topics from " + client.getUrl(), e);
      }
    }

    // TODO implement retry
//    if (CollectionUtils.isEmpty(topics)) {
//      throw new RuntimeException("No topics found, cannot build routes !!");
//    }

    for (Topic topic : topics) {
      // Path and header based route
      log.debug("Creating route for config: topic header {} {}", topic.getCode(), topic.getService());
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
}
