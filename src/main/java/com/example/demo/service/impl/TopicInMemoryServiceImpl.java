package com.example.demo.service.impl;

import com.example.demo.dto.Topic;
import com.example.demo.repository.InMemoryCache;
import com.example.demo.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Primary
public class TopicInMemoryServiceImpl implements TopicService {

    private final InMemoryCache inMemoryCache;

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
