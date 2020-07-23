package com.lti.micro.multiplexservice.exceptions;

/**
 * Exception class for handling no record found in DB
 */
public class NoRecordFoundException extends RuntimeException {
	
	/**
	 * Constructor to pass exception message
	 * @param message
	 */
	public NoRecordFoundException(String message) {
		super(message);
	}
}
