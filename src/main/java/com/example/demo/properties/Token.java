package com.example.demo.properties;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Token {

    private String salt;
    private int expiryInSec;
}
