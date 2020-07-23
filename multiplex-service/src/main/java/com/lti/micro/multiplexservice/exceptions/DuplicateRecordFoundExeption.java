package com.lti.micro.multiplexservice.exceptions;

/**
 * Exception class for handling duplicate record found in DB
 */
public class DuplicateRecordFoundExeption extends RuntimeException {
	
	/**
	 * Constructor to pass exception message
	 * @param message
	 */
	public DuplicateRecordFoundExeption(String message) {
		super(message);
	}

}
