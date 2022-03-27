package ru.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
public class RestConfiguration {

    @Bean
    @PostConstruct
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
