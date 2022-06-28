package com.example.demo.service;

import com.example.demo.dto.Topic;

import java.util.List;

public interface ClientTopicService {

    List<Topic> getTopicsByService(final String service);
}
