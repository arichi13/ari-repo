package com.lti.micro.multiplexservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.lti.micro.multiplexservice.document.MovieGenreType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class MovieDto {
	
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

	/*
	 * list of multiplex where this movie is showing
	 */
	private List<MultiplexDto> allocatedMultiplexes;
	
}
