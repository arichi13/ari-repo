package com.lti.micro.multiplexservice.controllers;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.lti.micro.multiplexservice.services.IMultiplexService;


@RunWith(SpringRunner.class)
@WebMvcTest(MultiplexController.class)
public class MultiplexControllerTest {
	
	@MockBean
	private IMultiplexService userService;
	
	@Autowired
	MockMvc mockMvc;
	

}
