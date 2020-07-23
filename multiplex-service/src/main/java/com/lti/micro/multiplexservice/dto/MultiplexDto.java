package com.lti.micro.multiplexservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MultiplexDto {
	
	private String multiplexId;
	
	private String multiplexName;
	 
	private int numberOfScreens;
	
	private String city;
	
	private List<String> screenNames;

}
