package com.example.demo.config;

import com.example.demo.dto.Topic;
import com.example.demo.properties.ApplicationProperties;
import com.example.demo.properties.Client;
import com.example.demo.service.ClientTopicService;
import com.example.demo.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class RouteConfig {

    private static final String TOPIC_HEADER_NAME = "topic";

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder,
                               ApplicationProperties applicationProperties,
                               ClientTopicService clientTopicService,
                               TopicService topicService) {

        if (applicationProperties == null || CollectionUtils.isEmpty(applicationProperties.getClients())) {
            log.error("clients properties not found");
            return builder.routes().build();
        }

        List<Topic> topics = new ArrayList<>();

        RouteLocatorBuilder.Builder routeLocatorBuilder = builder.routes();

        for (Client client : applicationProperties.getClients()) {
            // Path-based route
            log.debug("Creating route: path {} -> {}", client.getName(), client.getUrl());
            routeLocatorBuilder.route(r -> r.path("/" + client.getName() + "/**")
                    // strip prefix
//              .filters(f -> f.stripPrefix(1))
                    .filters(f -> f.rewritePath("^/" + client.getName(), ""))
                    .uri(client.getUrl()));

            try {
                List<Topic> t = clientTopicService.getTopicsByService(client.getUrl()).block();
                if (t != null) {
                    topics.addAll(t);
                }
            } catch (Exception e) {
                log.error("Cannot get topics from " + client.getUrl(), e);
            }
        }

        for (Topic topic : topics) {
            // Path and header based route
            log.debug("Creating route for config: topic header {} {}", topic.getCode(), topic.getService());
            routeLocatorBuilder.route(r ->
                    r.path("/config/**")
                            .and()
                            .header(TOPIC_HEADER_NAME, topic.getCode())
                            .filters(f ->
                                    // Test pre-, post- and retry filters (add header)
                                    f.addRequestHeader("X-forwarded-to", topic.getCode())
                                            .addResponseHeader("X-forwarded-from", topic.getCode())
                                            .retry(retryConfig ->
                                                    retryConfig.setRetries(3)
                                                            .setMethods(HttpMethod.GET)
                                                            .setBackoff(Duration.of(50, ChronoUnit.MILLIS), Duration.of(500, ChronoUnit.MILLIS), 2, false))
                            )
                            .uri(topic.getService())
            );
        }

        try {
            topicService.addTopicsToCache(topics);
        } catch (Exception e) {
            log.error("Could not add topics to cache", e);
        }

        return routeLocatorBuilder.build();
    }
}
