package com.example.demo.service.impl;

import com.example.demo.dto.Topic;
import com.example.demo.repository.InMemoryCache;
import com.example.demo.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Primary
public class TopicInMemoryServiceImpl implements TopicService {

    private static final String TOPICS_PATH = "/config/topics";

    private final InMemoryCache inMemoryCache;

    @Override
    public List<Topic> getTopicsByService(String service) {
        WebClient client = WebClient.create(service);
        Mono<List<Topic>> topicEntitiesMono = client.get()
                .uri(TOPICS_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        try {
            List<Topic> topics = topicEntitiesMono.block();
            if (topics != null) {
                topics.forEach(topic -> {
                    topic.setService(service);
                });
            }
            log.debug("{} topics: {}", service, topics);
            return topics;
        } catch (Exception e) {
            log.error("Cannot get topics from " + service);
            throw e;
        }
    }

    @Override
    public synchronized void addTopicsToCache(List<Topic> topics) {
        // Delete first all topics
        log.debug("Deleting topics from in-memory cache");
        inMemoryCache.getTopics().clear();

        log.debug("Topics to add to in-memory cache: {}", topics);
        for (Topic t : topics) {
            inMemoryCache.getTopics().put(t.getCode(), t);
        }

        log.debug("Topics in-memory cached: {}", topics);
    }

    @Override
    public List<Topic> getTopicsFromCache() {
        List<Topic> topics = new ArrayList<>(inMemoryCache.getTopics().values());
        log.info("gateway in-memory topics {}", topics);
        return topics;
    }
}
