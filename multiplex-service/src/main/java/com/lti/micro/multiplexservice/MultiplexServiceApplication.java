package com.lti.micro.multiplexservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Main starter for this micro service
 */
@SpringBootApplication
@EnableEurekaClient
public class MultiplexServiceApplication {
	
	/*
	 * Main method to boot start the app
	 */
	public static void main(String[] args) {
		SpringApplication.run(MultiplexServiceApplication.class, args);
	}

}
