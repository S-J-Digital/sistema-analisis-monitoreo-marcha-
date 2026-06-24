package com.example.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroservicioLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioLogApplication.class, args);
    }

}
