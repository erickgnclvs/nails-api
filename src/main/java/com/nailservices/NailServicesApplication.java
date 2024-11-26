package com.nailservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NailServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(NailServicesApplication.class, args);
    }
}
