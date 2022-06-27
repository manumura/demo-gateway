package com.example.demo.properties;

import com.example.demo.dto.Client;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "application")
@Component
@Getter
@Setter
public class ApplicationProperties {

    private List<Client> clients;
}
