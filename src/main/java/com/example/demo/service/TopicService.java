package com.example.demo.service;

import com.example.demo.dto.Topic;

import java.util.List;

public interface TopicService {

    void addTopicsToCache(List<Topic> topics);

    List<Topic> getTopicsFromCache();
}
