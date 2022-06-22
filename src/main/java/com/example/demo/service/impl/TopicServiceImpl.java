package com.example.demo.service.impl;

import com.example.demo.dto.Topic;
import com.example.demo.entity.TopicEntity;
import com.example.demo.mapper.TopicMapper;
import com.example.demo.repository.TopicRepository;
import com.example.demo.service.TopicService;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TopicServiceImpl implements TopicService {

    private static final String TOPICS_PATH = "/config/topics";

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    @Override
    public List<Topic> getTopicsByService(String service) {
        WebClient client = WebClient.create(service);
        Mono<List<Topic>> topicEntitiesMono1 = client.get()
                .uri(TOPICS_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        try {
            List<Topic> topics = topicEntitiesMono1.block();
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
    public void addTopicsToCache(List<Topic> topics) {
        try {
            // Delete first all topics
            log.debug("Deleting topics from cache");
            topicRepository.deleteAll();

            log.debug("Topics to add to cache: {}", topics);
            List<TopicEntity> topicEntitiesToCache = topicMapper.topicToTopicEntity(topics);

            Iterable<TopicEntity> topicEntitiesCached = topicRepository.saveAll(topicEntitiesToCache);
            log.debug("Topics cached: {}", topicEntitiesCached);
        } catch (RedisConnectionFailureException | RedisException e) {
            log.error("Could not add topics to cache", e);
        }
    }

    @Override
    public List<Topic> getTopicsFromCache() {
        try {
            final List<TopicEntity> topicEntities = IterableUtils.toList(topicRepository.findAll());
            final List<Topic> topics = topicMapper.topicEntityToTopic(topicEntities);
            log.info("gateway topics {}", topics);
            return topics;
        } catch (RedisConnectionFailureException | RedisException e) {
            log.error("Could not get topics from cache", e);
            return new ArrayList<>();
        }
    }
}
