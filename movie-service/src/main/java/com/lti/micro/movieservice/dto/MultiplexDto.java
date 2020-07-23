package com.lti.micro.movieservice.dto;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiplexDto {
	
	@Id
	private String multiplexId;
	
	private String multiplexName;
	 
	private Integer numberOfScreens;
	
	private String city;
	
	private List<String> screenNames;

}
