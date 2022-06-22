package com.example.demo.service;

import com.example.demo.dto.Topic;

import java.util.List;

public interface TopicService {

    List<Topic> getTopicsByService(final String service);

    void addTopicsToCache(List<Topic> topics);

    List<Topic> getTopicsFromCache();
}
