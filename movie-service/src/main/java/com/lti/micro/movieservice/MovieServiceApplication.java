package com.lti.micro.movieservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Main starter for this micro service
 */
@SpringBootApplication
@EnableEurekaClient
public class MovieServiceApplication {
	
	/*
	 * Main method to boot start the app
	 */
	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApplication.class, args);
	}

}
