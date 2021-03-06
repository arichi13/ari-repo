package com.lti.micro.movieservice.services;

import java.util.List;
import java.util.Optional;

import com.lti.micro.movieservice.dto.MovieDto;

/**
 * Interface to access the service methods this api provides
 */
public interface IMovieService {
	
	/**
	 * Returns all movies registered in the system
	 * @return list of movies
	 */
	public List<MovieDto> getAllMovies();
	
	/**
	 * Returns one or more movies searched with its name
	 * @param name movie name
	 * @return movie dto if found, null otherwise
	 */
	public List<MovieDto> getMovieByName(String name);
	
	/**
	 * Removes the given movie name from movie records
	 * @param name movie name
	 * @return Removed movie's name
	 */
	public String removeMovie(MovieDto movieDto);
	
	/**
	 * Update the record of the given movie 
	 * @param movieDto the movie to update
	 * @return successfully updated movie dto
	 */
	public int updateMovie (MovieDto movieDto);
	
	/**
	 * Create a new movie
	 * @param movie to create the new record from
	 */
	public MovieDto createMovie(MovieDto movie);

	/**
	 * Returns one or more movies starting with search name
	 * @param name movie name
	 * @return movie dto if found, null otherwise
	 */
	public List<MovieDto> getMovieStartingByName(String name);
	
	

}
