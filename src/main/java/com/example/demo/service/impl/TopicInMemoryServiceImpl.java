package com.example.demo.service.impl;

import com.example.demo.dto.Topic;
import com.example.demo.dto.TopicsInMemoryCache;
import com.example.demo.entity.TopicEntity;
import com.example.demo.mapper.TopicMapper;
import com.example.demo.repository.TopicRepository;
import com.example.demo.service.TopicService;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.RedisConnectionFailureException;
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

    private final TopicsInMemoryCache topicsInMemoryCache;

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
        log.debug("Deleting topics from cache");
        topicsInMemoryCache.getTopicsMap().clear();

        log.debug("Topics to add to cache: {}", topics);
        for (Topic t : topics) {
            topicsInMemoryCache.getTopicsMap().put(t.getCode(), t);
        }

        log.debug("Topics cached: {}", topics);
    }

    @Override
    public List<Topic> getTopicsFromCache() {
        List<Topic> topics = new ArrayList<>(topicsInMemoryCache.getTopicsMap().values());
        log.info("gateway topics {}", topics);
        return topics;
    }
}
