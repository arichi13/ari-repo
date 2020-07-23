package com.lti.micro.movieservice.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.lti.micro.movieservice.document.Movie;
import com.lti.micro.movieservice.dto.MovieWithMultiplexDto;
import com.mongodb.client.result.UpdateResult;

public class CustomMovieRepositoryImpl implements CustomMovieRepository{

	@Autowired
	MongoTemplate mongotemplate;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateMovie(MovieWithMultiplexDto movie) {
		Query queryMovie = new Query().addCriteria(Criteria.where("movieName").and("directorName").is(movie.getMovieName()));
		
		Update updateMovie = new Update()
				.set("movieName", movie.getMovieName())
				.set("movieGenre", movie.getMovieGenre())
				.set("producerName", movie.getProducerName())
				.set("directorName", movie.getDirectorName())
				.set("releaseDate", movie.getReleaseDate());
		UpdateResult updateResult = mongotemplate.updateFirst(queryMovie, updateMovie, Movie.class);
		return (int) updateResult.getModifiedCount();
	}

}
