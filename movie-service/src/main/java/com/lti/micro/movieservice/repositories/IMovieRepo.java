package com.lti.micro.movieservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lti.micro.movieservice.document.Movie;

@Repository
public interface IMovieRepo extends CrudRepository<Movie, String>, CustomMovieRepository {

	/**
	 * Custom method to search by exact name
	 * @param movieName
	 * @return found list of movies
	 */
	public List<Movie> findByMovieName(String movieName);
	
	/**
	 * Custom method to search by name and director name
	 * @param movieName and director name
	 * @return found record of movie
	 */
	public Optional<Movie> findByMovieNameAndDirectorName(String movieName, String directorName);
	
	/**
	 * Custom method to search by name starting with given search string
	 * @param movieName
	 * @return found list of movies
	 */
	public List<Movie> findByMovieNameStartingWithIgnoreCase(String movieName);
}
