package com.example.demo.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@Getter
@Setter
@ToString
public class TopicEntity implements Serializable {

    private static final long serialVersionUID = 4497026996490363658L;

    private Integer id;
    @Id
    private String code;
    private String service;
}
