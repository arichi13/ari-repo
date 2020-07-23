package com.lti.micro.multiplexservice.document;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Screen {
	
	private String screenId;
	
	private String screenName;
	
	private String multiplexId;
	
	private String movieId;
	

}
