package com.lti.micro.multiplexservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lti.micro.multiplexservice.document.Screen;

@Repository
public interface ScreenRepository extends MongoRepository<Screen, String> {

	List<Screen> findByMultiplexIdAndMovieIdNull(String multiplexId);

	List<Screen> findByMultiplexId(String multiplexId);


	Optional<Screen> findByMultiplexIdAndScreenName(String multiplexId, String screenId);
	
	int deleteByMultiplexId(String multiplexId);
	
	int deleteByScreenNameAndMultiplexId(String screenName, String multiplexId);

	int deleteByMovieId(String movieId);

	int deleteAllByMultiplexId(String multiplexId);

	List<Screen> findByMovieId(String movieId);

}
