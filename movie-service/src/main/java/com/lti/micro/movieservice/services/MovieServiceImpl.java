package com.lti.micro.movieservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.lti.micro.movieservice.document.Movie;
import com.lti.micro.movieservice.dto.MovieDto;
import com.lti.micro.movieservice.repositories.IMovieRepo;

/**
 * Implementation class for all service methods  
 * @author Reliance
 *
 */
public class MovieServiceImpl implements IMovieService {
	
	@Autowired
	IMovieRepo repo;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MovieDto> getAllMovies() {
		Iterable<Movie> allMovies = repo.findAll();
		ArrayList<MovieDto> listToReturn = new ArrayList<MovieDto>();
		allMovies.forEach((movie) ->
		{
			listToReturn.add(new MovieDto(movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), movie.getReleaseDate(), null));
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
			listToReturn.add(new MovieDto(movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), movie.getReleaseDate(), null));
		});	
		return listToReturn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String removeMovie(MovieDto movieDto) {
		Optional<Movie> foundMovie = repo.findByMovieNameAndDirectorName(movieDto.getMovieName(), movieDto.getDirectorName());
		foundMovie.map((movie) -> {
			repo.delete(movie);
			return movie.getMovieName();
		});
		return null;	

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateMovie(MovieDto movieDto) {
		return repo.updateMovie(movieDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MovieDto createMovie(MovieDto movieDto) {
		Optional<Movie> foundMovie = repo.findByMovieNameAndDirectorName(movieDto.getMovieName(), movieDto.getDirectorName());
		
		if(!foundMovie.isPresent()){
			Movie insertedMovie = repo.save(new Movie(null, movieDto.getMovieName(), movieDto.getMovieGenre(), movieDto.getDirectorName(), movieDto.getProducerName(), movieDto.getReleaseDate()));
			Optional<Movie> nullableMovie = Optional.ofNullable(insertedMovie);
			nullableMovie.map((value) -> {
				return new MovieDto(value.getMovieName(), movieDto.getMovieGenre(), movieDto.getDirectorName(), movieDto.getProducerName(), movieDto.getReleaseDate(), null);
			});
		}
		return null;
	}

	@Override
	public List<MovieDto> getMovieByName(String name) {
		List<Movie> returnedMovies = repo.findByMovieName(name);
		ArrayList<MovieDto> listToReturn = new ArrayList<MovieDto>();
		returnedMovies.forEach((movie) -> {
			listToReturn.add(new MovieDto(movie.getMovieName(), movie.getMovieGenre(), movie.getDirectorName(), movie.getProducerName(), movie.getReleaseDate(), null));
		});	
		return listToReturn;
	}
	
}
