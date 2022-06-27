package com.example.demo.repository;

import com.example.demo.dto.Topic;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class InMemoryCache {

    private Map<String, Topic> topics = new HashMap<>();
}
