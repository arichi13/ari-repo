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
			listToReturn.add(new MovieWithMultiplexDto(movie.getMovieId(), movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), formatter.format(movie.getReleaseDate()), null));
		});
		return listToReturn;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MovieDto> getMovieStartingByName(String name) {
		List<Movie> returnedMovies = repo.findByMovieNameStartingWith(name);
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
	
	public List<MovieWithMultiplexDto> getMovieByName(String name) {
		List<Movie> returnedMovies = repo.findByMovieName(name);
		ArrayList<MovieWithMultiplexDto> listToReturn = new ArrayList<MovieWithMultiplexDto>();
		returnedMovies.forEach((movie) -> {
			ResponseEntity<List<MultiplexDto>> returnedMultiplexId = getMultiplexes(movie.getMovieId());
			List<MultiplexDto> linkedMultiplexes = null;
			if(returnedMultiplexId.getStatusCode().equals(HttpStatus.OK)) {
				linkedMultiplexes = returnedMultiplexId.getBody();
			}
			listToReturn.add(new MovieWithMultiplexDto(movie.getMovieId(), movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), formatter.format(movie.getReleaseDate()), linkedMultiplexes));
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String removeMovie(MovieDto movieDto) {
		Optional<Movie> foundMovie = repo.findById(movieDto.getMovieId());
		System.out.println("found movie " + foundMovie.isPresent());
		Optional<String> removedMovie = foundMovie.map((movie) -> {
			ResponseEntity<String> returnedResult = multiplexProxy.removeAllotmentForMovie(movie.getMovieId());
			if(returnedResult.getStatusCode().equals(HttpStatus.OK)) {
				String removalResult = returnedResult.getBody();
				repo.delete(movie);
				System.out.println("allotment removed :" + removalResult);
				return movieDto.getMovieName();
			}
			return null;
		});
		return removedMovie.isPresent() ? movieDto.getMovieName() : null;	

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateMovie(MovieDto movieDto) {
		Optional<Movie> foundMovie = repo.findById(movieDto.getMovieId());
		System.out.println("found movie " + foundMovie.isPresent());
		Optional<Integer> updateResult = foundMovie.map((movie) -> {
			movie.setMovieName(movieDto.getMovieName());
			movie.setDirectorName(movieDto.getDirectorName());
			movie.setProducerName(movieDto.getProducerName());
			movie.setMovieGenre(movieDto.getMovieGenre());
			movie.setReleaseDate(LocalDate.parse(movieDto.getReleaseDate(), formatter));
			repo.save(movie);
			return 1;
		});
		return updateResult.isPresent() ? 1 : 0;
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
				return new MovieDto(insertedMovie.getMovieId(), insertedMovie.getMovieName(), movieDto.getMovieGenre(), movieDto.getDirectorName(), movieDto.getProducerName(), movieDto.getReleaseDate());
			}
		}
		return null;
	}

	
	
}
