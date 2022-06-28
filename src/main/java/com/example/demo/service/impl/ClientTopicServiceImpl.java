package com.example.demo.service.impl;

import com.example.demo.constant.Constant;
import com.example.demo.dto.Topic;
import com.example.demo.dto.User;
import com.example.demo.security.InternalTokenService;
import com.example.demo.service.ClientTopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientTopicServiceImpl implements ClientTopicService {

    private static final String TOPICS_PATH = "/config/topics";

    private final InternalTokenService internalTokenService;

    @Override
    public List<Topic> getTopicsByService(String service) {
        WebClient client = WebClient.create(service);
        Mono<List<Topic>> topicEntitiesMono = client.get()
                .uri(TOPICS_PATH)
                .headers(h -> h.setBearerAuth(internalTokenService.generateInternalUserToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        try {
            List<Topic> topics = topicEntitiesMono.block();
            if (topics != null) {
                topics.forEach(topic -> topic.setService(service));
            }
            log.debug("{} topics: {}", service, topics);
            return topics;
        } catch (Exception e) {
            log.error("Cannot get topics from " + service);
            throw e;
        }
    }
}
