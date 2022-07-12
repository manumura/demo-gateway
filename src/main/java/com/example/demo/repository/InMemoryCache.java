package com.example.demo.repository;

import com.example.demo.dto.Topic;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Data
public class InMemoryCache {

    private Map<String, Topic> topics = new ConcurrentHashMap<>();
}
