package com.example.demo.mapper;

import com.example.demo.dto.Topic;
import com.example.demo.entity.TopicEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TopicMapper {

  TopicEntity topicToTopicEntity(Topic topic);

  List<TopicEntity> topicToTopicEntity(List<Topic> topics);

  Topic topicEntityToTopic(TopicEntity topicEntity);

  List<Topic> topicEntityToTopic(List<TopicEntity> topicEntities);
}
