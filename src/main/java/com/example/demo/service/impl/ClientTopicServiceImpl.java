package com.example.demo.service.impl;

import com.example.demo.dto.Topic;
import com.example.demo.security.TokenGeneratorService;
import com.example.demo.service.ClientTopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientTopicServiceImpl implements ClientTopicService {

    private static final String TOPICS_PATH = "/config/topics";

    private final TokenGeneratorService tokenGeneratorService;

    @Override
    public Mono<List<Topic>> getTopicsByService(String service) {
        WebClient client = WebClient.create(service);
        return client.get()
                .uri(TOPICS_PATH)
                .headers(h -> h.setBearerAuth(tokenGeneratorService.generateInternalUserToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Topic>>() {})
                // TODO retry https://medium.com/nerd-for-tech/webclient-error-handling-made-easy-4062dcf58c49
                .onErrorResume(Exception.class, e -> {
                    log.error(e.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .doOnSuccess(topics -> {
                    topics.forEach(topic -> topic.setService(service));
                    log.debug("client {} topics: {}", service, topics);
                });
    }
}
