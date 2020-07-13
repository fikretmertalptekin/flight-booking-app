package com.travel.flightbookingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;

@SpringBootApplication(exclude = HypermediaAutoConfiguration.class)
public class FlightbookingappApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightbookingappApplication.class, args);
    }

}
