package com.lti.micro.movieservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lti.micro.movieservice.dto.MovieDto;
import com.lti.micro.movieservice.dto.MovieExceptionDto;
import com.lti.micro.movieservice.dto.MovieWithMultiplexDto;
import com.lti.micro.movieservice.exceptions.DuplicateRecordFoundExeption;
import com.lti.micro.movieservice.exceptions.NoRecordFoundException;
import com.lti.micro.movieservice.services.IMovieService;

/**
 * Main controller exposing REST methods to perform operations on movie entity.
 * api : /movie-mgmt
 */
@RestController
@RequestMapping("/movie-mgmt")
@CrossOrigin(origins = "*")
public class MovieController {
	
	/*
	 * Autowired dependency of the the service interface.
	 */
	@Autowired
	IMovieService movieService;
	
	@GetMapping("/adminLogin")
	public ResponseEntity<String> loginAsAdmin(@RequestParam String username, @RequestParam String password ){
		System.out.println("Login called");
		System.out.println(username + " " + password);
		return new ResponseEntity<String>("Admin login successful", HttpStatus.OK);
	}
	
	/**
	 * Get movie by name
	 * @param movieName
	 * @return
	 */
	@GetMapping("/searchById/{movieId}")
	public ResponseEntity<MovieWithMultiplexDto> getMovieById(@PathVariable String movieId){
		MovieWithMultiplexDto foundMovie = movieService.getMovieById(movieId);
		if(foundMovie == null){
			throw new NoRecordFoundException("No records found");
		}
		System.out.println("searchById" + foundMovie);
		return new ResponseEntity<MovieWithMultiplexDto>(foundMovie, HttpStatus.OK);
	}
	
	/**
	 * Get movie by name
	 * @param movieName
	 * @return
	 */
	@GetMapping("/searchByName/{movieName}")
	public ResponseEntity<List<MovieDto>> getMovie(@PathVariable String movieName){
		List<MovieDto> foundMovies = movieService.getMovieByName(movieName);
		if(foundMovies.size()==0){
			throw new NoRecordFoundException("No records found");
		}
		return new ResponseEntity<List<MovieDto>>(foundMovies, HttpStatus.OK);
	}
	
	
	/**
	 * Get specific movie 
	 * @param movieName
	 * @return
	 */
	@GetMapping("/searchSync/{movieName}")
	public ResponseEntity<List<MovieDto>> getMovieInstantly(@PathVariable String movieName){
		List<MovieDto> foundMovies = movieService.getMovieStartingByName(movieName);
		if(foundMovies.size()==0){
			throw new NoRecordFoundException("No records found");
		}
		return new ResponseEntity<List<MovieDto>>(foundMovies, HttpStatus.OK);
	}
	/**
	 * Retrieve all movies
	 * @return list of movie
	 */
	@GetMapping("/admin/getAll")
	public ResponseEntity<List<MovieWithMultiplexDto>> getAllMovies() {
		return new ResponseEntity<List<MovieWithMultiplexDto>>(movieService.getAllMovies(), HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param newMovie
	 * @return
	 */
	@PostMapping("/admin/addMovie")
	public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto newMovie){
		MovieDto insertedMovie = movieService.createMovie(newMovie);
		System.out.println("Inserted Movie" + insertedMovie);
		if(insertedMovie == null) {
			throw new DuplicateRecordFoundExeption("Movie with same name and director already exits");
		}
		return new ResponseEntity<MovieDto>(insertedMovie, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param movieToDelete
	 * @return
	 */
	@DeleteMapping("/admin/removeMovie/{movieId}")
	public ResponseEntity<String> removeMovie(@PathVariable String movieId){
		String returnedString = movieService.removeMovie(movieId);
		if(returnedString == null) {
			return new ResponseEntity<String>("Record not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(returnedString, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param movieToUpdate
	 * @return
	 */
	@PostMapping("/admin/updateMovie")
	public ResponseEntity<MovieDto> updateMovie(@RequestBody MovieDto movieToUpdate) {
		MovieDto returnedDto = movieService.updateMovie(movieToUpdate);
		if(returnedDto ==null) {
			throw new NoRecordFoundException("No records found");
		}
		return new ResponseEntity<MovieDto>(returnedDto, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 */
	@ExceptionHandler(NoRecordFoundException.class)
	public ResponseEntity<MovieExceptionDto> exceptionHandler(NoRecordFoundException ex) {
		MovieExceptionDto exDto = new MovieExceptionDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<MovieExceptionDto>(exDto,HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 
	 * @return
	 */
	@ExceptionHandler(DuplicateRecordFoundExeption.class)
	public ResponseEntity<MovieExceptionDto> exceptionHandler(DuplicateRecordFoundExeption ex) {
		MovieExceptionDto exDto = new MovieExceptionDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<MovieExceptionDto>(exDto,HttpStatus.BAD_REQUEST);
	}
	

}
