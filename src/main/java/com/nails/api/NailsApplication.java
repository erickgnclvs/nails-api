package com.nails.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NailsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NailsApplication.class, args);
    }
}
