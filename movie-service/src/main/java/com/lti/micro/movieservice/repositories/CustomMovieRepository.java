package com.lti.micro.movieservice.repositories;

import com.lti.micro.movieservice.dto.MovieWithMultiplexDto;

public interface CustomMovieRepository {
	
	/**
	 * Update the movie record given by the argument movie dto
	 * @param movie
	 * @return 1 if updated successfully
	 */
	public int updateMovie(MovieWithMultiplexDto movie);
	
}
