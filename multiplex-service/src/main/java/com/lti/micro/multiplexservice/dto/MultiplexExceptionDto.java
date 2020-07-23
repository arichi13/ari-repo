package com.lti.micro.multiplexservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MultiplexExceptionDto {
	
	/*
	 * The message for this exception
	 */
	private String message;
	
	/*
	 * The http error code
	 */
	private Integer errorCode;

}
