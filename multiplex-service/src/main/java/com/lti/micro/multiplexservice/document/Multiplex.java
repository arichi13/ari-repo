package com.lti.micro.multiplexservice.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Multiplex {
	
	@Id
	/*
	 * Identifier for this mulitplex
	 */
	private String multiplexId;
	
	/*
	 * 	Name of mulitplex
	 */
	private String multiplexName;
	
	/*
	 * Number of screens in this multiplex
	 */
	private int numberOfScreens;
	
	/*
	 * City of this mulitplex
	 */
	private String city;
	
	

}
