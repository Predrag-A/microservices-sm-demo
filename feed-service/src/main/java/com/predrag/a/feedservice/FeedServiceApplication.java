package com.predrag.a.feedservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FeedServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(FeedServiceApplication.class, args);
    }
}
