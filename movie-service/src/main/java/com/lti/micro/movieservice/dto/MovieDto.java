package com.lti.micro.movieservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.lti.micro.movieservice.document.MovieGenreType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
	
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
	private String releaseDate;


}
