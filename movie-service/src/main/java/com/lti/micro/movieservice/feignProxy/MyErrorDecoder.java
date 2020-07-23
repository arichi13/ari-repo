package com.lti.micro.movieservice.feignProxy;

import feign.Response;
import feign.codec.ErrorDecoder;

public class MyErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		// TODO Auto-generated method stub
		return null;
	}

}
