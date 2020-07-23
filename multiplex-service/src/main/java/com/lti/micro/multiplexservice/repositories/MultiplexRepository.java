package com.lti.micro.multiplexservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lti.micro.multiplexservice.document.Multiplex;

@Repository
public interface MultiplexRepository extends CrudRepository<Multiplex, String> {

	/**
	 * Custom method to search by exact name
	 * @param multiplexName
	 * @return found list of multiplexes
	 */
	public List<Multiplex> findByMultiplexName(String multiplexName);
	
	/**
	 * Custom method to find unique multiplex by id and city
	 * @param multiplexName
	 * @param city
	 * @return optional multiplex
	 */
	public Optional<Multiplex> findByMultiplexIdAndMultiplexName(String multiplexid, String multiplexName);
	
	/**
	 * Custom method to find unique multiplex by name and city
	 * @param multiplexName
	 * @param city
	 * @return optional multiplex
	 */
	public Optional<Multiplex> findByMultiplexNameAndCity(String multiplexName, String city);
	
	/**
	 * Custom method to search by name starting with given search string
	 * @param multiplexName
	 * @return found list of movies
	 */
	public List<Multiplex> findByMultiplexNameStartingWith(String multiplexName);
}
