package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
//import org.springframework.data.redis.core.RedisHash;

//@RedisHash
@Entity
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
