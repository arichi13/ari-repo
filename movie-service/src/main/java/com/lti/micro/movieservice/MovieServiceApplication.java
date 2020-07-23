package com.lti.micro.movieservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main starter for this micro service
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.lti.micro.movieservice.feignProxy")
@EnableCircuitBreaker
public class MovieServiceApplication {
	
	/*
	 * Main method to boot start the app
	 */
	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApplication.class, args);
	}
	
	/*@Bean
	public MyErrorDecoder myErrorDecoder() {
	  return new MyErrorDecoder();
	}*/

}
