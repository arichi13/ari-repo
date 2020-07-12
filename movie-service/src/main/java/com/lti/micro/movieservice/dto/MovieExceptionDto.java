package com.lti.micro.movieservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieExceptionDto {
	
	/*
	 * The message for this exception
	 */
	private String message;
	
	/*
	 * The http error code
	 */
	private Integer errorCode;

}
