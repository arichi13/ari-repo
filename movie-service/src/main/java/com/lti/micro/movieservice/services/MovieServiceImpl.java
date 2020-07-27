package com.lti.micro.movieservice.services;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lti.micro.movieservice.document.Movie;
import com.lti.micro.movieservice.dto.MovieDto;
import com.lti.micro.movieservice.dto.MovieWithMultiplexDto;
import com.lti.micro.movieservice.dto.MultiplexDto;
import com.lti.micro.movieservice.feignProxy.MultiplexFeignProxy;
import com.lti.micro.movieservice.repositories.IMovieRepo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * Implementation class for all service methods  
 *
 */
@Service
public class MovieServiceImpl implements IMovieService {
	
	@Autowired
	IMovieRepo repo;
	
	@Autowired 
	MultiplexFeignProxy multiplexProxy;

	/**
	 * Custom formatter for date<->string conversions
	 */
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MovieWithMultiplexDto> getAllMovies() {
		Iterable<Movie> allMovies = repo.findAll();
		ArrayList<MovieWithMultiplexDto> listToReturn = new ArrayList<MovieWithMultiplexDto>();
		allMovies.forEach((movie) ->
		{
			ResponseEntity<List<MultiplexDto>> returnedMultiplexId = getMultiplexes(movie.getMovieId());
			List<MultiplexDto> linkedMultiplexes = null;
			if(returnedMultiplexId.getStatusCode().equals(HttpStatus.OK)) {
				linkedMultiplexes = returnedMultiplexId.getBody();
			}
			listToReturn.add(new MovieWithMultiplexDto(movie.getMovieId(), movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), formatter.format(movie.getReleaseDate()), linkedMultiplexes));
		});
		return listToReturn;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MovieDto> getMovieStartingByName(String name) {
		List<Movie> returnedMovies = repo.findByMovieNameStartingWithIgnoreCase(name);
		ArrayList<MovieDto> listToReturn = new ArrayList<MovieDto>();
		returnedMovies.forEach((movie) -> {
			listToReturn.add(new MovieDto(movie.getMovieId(), movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), formatter.format(movie.getReleaseDate())));
		});	
		return listToReturn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	
	public List<MovieDto> getMovieByName(String name) {
		List<Movie> returnedMovies = repo.findByMovieName(name);
		ArrayList<MovieDto> listToReturn = new ArrayList<MovieDto>();
		returnedMovies.forEach((movie) -> {
			listToReturn.add(new MovieDto(movie.getMovieId(), movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), formatter.format(movie.getReleaseDate())));
		});	
		return listToReturn;
	}
	
	@HystrixCommand(fallbackMethod ="getFallbackMultiplex")
	public ResponseEntity<List<MultiplexDto>> getMultiplexes(String movieId){
		return multiplexProxy.getMultiplexesForMovie(movieId);
	}
	
	public ResponseEntity<List<MultiplexDto>> getFallbackMultiplex(String name){
		return new ResponseEntity<List<MultiplexDto>>(Collections.emptyList(), HttpStatus.OK);
	}
	
	
	@Override
	public MovieWithMultiplexDto getMovieById(String movieId) {
		Optional<Movie> foundmovie = repo.findById(movieId);
		if(foundmovie.isPresent()) {
			Movie movie = foundmovie.get();
			ResponseEntity<List<MultiplexDto>> returnedMultiplexId = getMultiplexes(movieId);
			List<MultiplexDto> linkedMultiplexes = null;
			if(returnedMultiplexId.getStatusCode().equals(HttpStatus.OK)) {
				linkedMultiplexes = returnedMultiplexId.getBody();
			}
			return new MovieWithMultiplexDto(movie.getMovieId(), movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), formatter.format(movie.getReleaseDate()), linkedMultiplexes);
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String removeMovie(String movieId) {
		Optional<Movie> foundMovie = repo.findById(movieId);
		System.out.println("found movie " + foundMovie.isPresent());
		Optional<String> removedMovie = foundMovie.map((movie) -> {
			ResponseEntity<String> returnedResult = multiplexProxy.removeAllotmentForMovie(movieId);
			if(returnedResult.getStatusCode().equals(HttpStatus.OK)) {
				String removalResult = returnedResult.getBody();
				repo.delete(movie);
				System.out.println("allotment removed :" + removalResult);
				return movie.getMovieName();
			}
			return null;
		});
		return removedMovie.isPresent() ? removedMovie.get() : null;	

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MovieDto updateMovie(MovieDto movieDto) {
		Optional<Movie> foundMovie = repo.findById(movieDto.getMovieId());
		System.out.println("found movie " + foundMovie.isPresent());
		Optional<MovieDto> updateResult = foundMovie.map((movie) -> {
			movie.setMovieName(movieDto.getMovieName());
			movie.setDirectorName(movieDto.getDirectorName());
			movie.setProducerName(movieDto.getProducerName());
			movie.setMovieGenre(movieDto.getMovieGenre());
			movie.setReleaseDate(LocalDate.parse(movieDto.getReleaseDate(), formatter));
			Movie updatedMovie = repo.save(movie);
			return new MovieDto(updatedMovie.getMovieId(), updatedMovie.getMovieName(), updatedMovie.getMovieGenre(), updatedMovie.getDirectorName(), updatedMovie.getProducerName(), formatter.format(updatedMovie.getReleaseDate()));
		});
		return updateResult.isPresent() ? updateResult.get() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MovieDto createMovie(MovieDto movieDto) {
		Optional<Movie> foundMovie = repo.findByMovieNameAndDirectorName(movieDto.getMovieName(), movieDto.getDirectorName());
		System.out.println("found movie " + foundMovie.isPresent());
		if(!foundMovie.isPresent()){
			Movie insertedMovie = repo.save(new Movie(null, movieDto.getMovieName(), movieDto.getMovieGenre(), movieDto.getDirectorName(), movieDto.getProducerName(), LocalDate.parse(movieDto.getReleaseDate(), formatter)));
			System.out.println("After save "+insertedMovie);
			if(insertedMovie != null) {
				return new MovieDto(insertedMovie.getMovieId(), insertedMovie.getMovieName(), insertedMovie.getMovieGenre(), insertedMovie.getDirectorName(), insertedMovie.getProducerName(), formatter.format(insertedMovie.getReleaseDate()));
			}
		}
		return null;
	}

	

	
	
}
