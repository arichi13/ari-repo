package com.lti.micro.multiplexservice.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="api-gateway")
@RibbonClient(name="micro-movie")
public interface MovieFeignProxy {

}
