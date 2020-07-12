package com.lti.micro.movieservice.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Document
@Data
@ToString
@AllArgsConstructor
public class Movie {
	
	@Id
	/*
	 * Identifier for this movie
	 */
	private String movieId;
	
	/*
	 * 	Name of movie
	 */
	private String movieName;
	
	/*
	 * Genre of movie
	 */
	private MovieGenreType movieGenre;
	
	/*
	 * Director of this movie
	 */
	private String directorName;
	
	/*
	 * Producer of this movie
	 */
	private String producerName;
	
	/*
	 * Release date of this movie
	 */
	private LocalDateTime releaseDate;
	

}
