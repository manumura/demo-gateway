package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;
import javax.persistence.Entity;
import javax.persistence.Id;

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
