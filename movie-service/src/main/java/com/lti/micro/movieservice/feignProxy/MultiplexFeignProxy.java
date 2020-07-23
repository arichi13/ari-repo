package com.lti.micro.movieservice.feignProxy;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.lti.micro.movieservice.dto.MultiplexDto;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name="api-gateway", fallbackFactory = MultiplexFallbackFactory.class)
@RibbonClient(name="micro-multiplex")
public interface MultiplexFeignProxy {
	

	@GetMapping("micro-multiplex/multiplex-mgmt/getMultiplexForMovie/{movieId}")
	public ResponseEntity<List<MultiplexDto>> getMultiplexesForMovie(@PathVariable("movieId") String movieId);
	
	@DeleteMapping("micro-multiplex/multiplex-mgmt/admin/removeAllotmentForMovie")
	public ResponseEntity<String> removeAllotmentForMovie(@RequestBody String movieId);
}

