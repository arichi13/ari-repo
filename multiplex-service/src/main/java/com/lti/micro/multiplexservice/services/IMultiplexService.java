package com.lti.micro.multiplexservice.services;

import java.util.List;

import com.lti.micro.multiplexservice.dto.MultiplexDto;
import com.lti.micro.multiplexservice.dto.ScreenDto;

/**
 * Interface to access the service methods this api provides
 */
public interface IMultiplexService {
	
	/**
	 * Returns all movies registered in the system
	 * @return list of movies
	 */
	public List<MultiplexDto> getAllMultiplexes();
	
	/**
	 * Returns one or more movies searched with its name
	 * @param name movie name
	 * @return movie dto if found, null otherwise
	 */
	public List<MultiplexDto> getMultiplexByName(String name);
	
	/**
	 * Removes the given movie name from movie records
	 * @param name movie name
	 * @return Removed movie's name
	 */
	public String removeMultiplex(MultiplexDto movieDto);
	
	/**
	 * Update the record of the given movie 
	 * @param movieDto the movie to update
	 * @return successfully updated movie dto
	 */
	public int updateMultiplex (MultiplexDto movieDto);
	
	/**
	 * Create a new movie
	 * @param movie to create the new record from
	 */
	public String createMultiplex(MultiplexDto movie);

	/**
	 * Returns one or more movies starting with search name
	 * @param name movie name
	 * @return movie dto if found, null otherwise
	 */
	public List<MultiplexDto> getMultiplexStartingByName(String name);
	
	/**
	 * Allot given movie id to multiplex id with the screen id
	 * @param movieId
	 * @param multiplexId
	 * @param screenId
	 * @return 1 if successful
	 */
	public int allotMovieToMultiplex(String movieId, String multiplexId, String screenId);
	
	/**
	 * Get available screens for this multiplex
	 * @param multiplexId
	 * @return list of screens
	 */
	public List<String> getAvailableScreens(String multiplexId);
	
	/**
	 * Get multiplexes for this movie
	 * @param movieId
	 * @return list of multiplex names
	 */

	public List<MultiplexDto> getMultiplexForMovie(String movieId);

	/**
	 * Remove allotment for movie
	 * @param movieId
	 * @return
	 */
	public String removeAllotment(String movieId);
	
	

}
