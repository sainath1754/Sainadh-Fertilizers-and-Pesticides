package com.sainadh.fertilizers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FertilizersApplication {

    public static void main(String[] args) {
        SpringApplication.run(FertilizersApplication.class, args);
    }
}