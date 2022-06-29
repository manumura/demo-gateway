package com.example.demo.service;

import com.example.demo.dto.Topic;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ClientTopicService {

    Mono<List<Topic>> getTopicsByService(final String service);
}
