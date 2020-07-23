package com.lti.gateway.moviemultiplexapigateway;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class ApiFilter extends ZuulFilter {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Object run() throws ZuulException {
				// what to do in case request is intercepted
				HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
				log.info("Zuul Intercepts : " + request.getRequestURL()+" " + request.getParameter("username")) ;
			    log.info("Zuul Intercepts : " + request.getHeader("authorization"));   
				return null;
	}

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

}
