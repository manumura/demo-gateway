package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;

//@RedisHash
@Entity
@Getter
@Setter
@ToString
public class TopicEntity implements Serializable {

    private static final long serialVersionUID = 4497026996490363658L;

    @Id
    private String uuid;
    private String code;
    private String service;
}
