package com.predrag.a.mediaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MediaServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MediaServiceApplication.class, args);
    }

}
