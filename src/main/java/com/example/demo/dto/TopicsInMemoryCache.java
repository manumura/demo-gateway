package com.example.demo.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class TopicsInMemoryCache {

    private Map<String, Topic> topicsMap = new HashMap<>();
}
