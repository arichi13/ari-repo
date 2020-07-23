package com.lti.micro.movieservice.feignProxy;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.lti.micro.movieservice.dto.MultiplexDto;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@Component
public class MultiplexFallbackFactory implements FallbackFactory<MultiplexFeignProxy>  {
	
	 @Override
	    public MultiplexFeignProxy create(Throwable cause) {

	     String httpStatus = cause instanceof FeignException ? Integer.toString(((FeignException) cause).status()) : "";

	     return new MultiplexFeignProxy() {
	       
			@Override
			public ResponseEntity<List<MultiplexDto>> getMultiplexesForMovie(String movieId) {
				// TODO Auto-generated method stub
				return new ResponseEntity<List<MultiplexDto>>(Collections.emptyList(), HttpStatus.valueOf(httpStatus));
			}

			@Override
			public ResponseEntity<String> removeAllotmentForMovie(String movieId) {
				// TODO Auto-generated method stub
				return new ResponseEntity<String>("Record not found", HttpStatus.NOT_FOUND);
			}
	        
	        
	    };

	 }
}
