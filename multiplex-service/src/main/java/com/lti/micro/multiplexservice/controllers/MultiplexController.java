package com.lti.micro.multiplexservice.controllers;

import java.util.Collections;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lti.micro.multiplexservice.dto.MultiplexDto;
import com.lti.micro.multiplexservice.dto.MultiplexExceptionDto;
import com.lti.micro.multiplexservice.exceptions.DuplicateRecordFoundExeption;
import com.lti.micro.multiplexservice.exceptions.NoRecordFoundException;
import com.lti.micro.multiplexservice.services.IMultiplexService;

/**
 * Main controller exposing REST methods to perform operations on multiplex entity.
 * api : /multiplex-mgmt
 */
@RestController
@RequestMapping("/multiplex-mgmt")
@CrossOrigin(origins = "*")
public class MultiplexController {
	
	/*
	 * Autowired dependency of the the service interface.
	 */
	@Autowired
	IMultiplexService multiplexService;
	
	/**
	 * Get specific multiplex 
	 * @param multiplexName
	 * @return
	 */
	@GetMapping("/search/{multiplexName}")
	public ResponseEntity<List<MultiplexDto>> getMultiplex(@PathVariable String multiplexName){
		List<MultiplexDto> foundMultiplexs = multiplexService.getMultiplexByName(multiplexName);
		if(foundMultiplexs.size()==0){
			throw new NoRecordFoundException("No records found");
		}
		return new ResponseEntity<List<MultiplexDto>>(foundMultiplexs, HttpStatus.OK);
	}
	
	/**
	 * Get specific multiplex 
	 * @param multiplexName
	 * @return
	 */
	@GetMapping("/searchSync/{multiplexName}")
	public ResponseEntity<List<MultiplexDto>> getMultiplexInstantly(@PathVariable String multiplexName){
		List<MultiplexDto> foundMultiplexs = multiplexService.getMultiplexStartingByName(multiplexName);
		if(foundMultiplexs.size()==0){
			throw new NoRecordFoundException("No records found");
		}
		return new ResponseEntity<List<MultiplexDto>>(foundMultiplexs, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param newMultiplex
	 * @return
	 */
	@PostMapping("/admin/addMultiplex")
	public ResponseEntity<String> createMultiplex(@RequestBody MultiplexDto newMultiplex){
		String insertedMultiplex = multiplexService.createMultiplex(newMultiplex);
		if(insertedMultiplex == null) {
			throw new DuplicateRecordFoundExeption("Multiplex with same name and city already exits");
		}
		return new ResponseEntity<String>(insertedMultiplex, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param multiplexToDelete
	 * @return
	 */
	@DeleteMapping("/admin/removeMultiplex")
	public ResponseEntity<String> removeMultiplex(@RequestBody MultiplexDto multiplexToDelete){
		String returnedString = multiplexService.removeMultiplex(multiplexToDelete);
		if(returnedString == null) {
			return new ResponseEntity<String>("Record not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(returnedString, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param multiplexToUpdate
	 * @return
	 */
	@PutMapping("/admin/updateMultiplex")
	public ResponseEntity<Integer> updateMultiplex(@RequestBody MultiplexDto multiplexToUpdate) {
		if( multiplexService.updateMultiplex(multiplexToUpdate) >0) {
			return new ResponseEntity<Integer>(1, HttpStatus.OK);
		}
		return new ResponseEntity<Integer>(0, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/admin/getAvailableScreens/{multiplexId}")
	public ResponseEntity<List<String>> getAvailableScreens(@PathVariable String multiplexId) {
		List<String> availableScreens = multiplexService.getAvailableScreens(multiplexId);
		if(availableScreens.isEmpty()) {
			throw new NoRecordFoundException("No Screens Available");
		}
		return new ResponseEntity<List<String>>(availableScreens, HttpStatus.OK);
	}
	
	@PutMapping("/admin/allot/{multiplexId}/{screenId}/{movieId}")
	public ResponseEntity<Integer> allotMovie(@PathVariable String multiplexId, @PathVariable String screenId, @PathVariable String movieId ){
		int allotmentSuccess = multiplexService.allotMovieToMultiplex(movieId, multiplexId, screenId);
		if(allotmentSuccess == 0){
			return new ResponseEntity<Integer>(allotmentSuccess, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(allotmentSuccess, HttpStatus.OK);
	}
	
	@GetMapping("/getMultiplexForMovie/{movieId}")
	public ResponseEntity<List<MultiplexDto>> getMultiplexesForMovie(@PathVariable String movieId){
		List<MultiplexDto> multiplexList = multiplexService.getMultiplexForMovie(movieId);
		if(multiplexList.size()==0){
			return new ResponseEntity<List<MultiplexDto>>(Collections.emptyList(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<MultiplexDto>>(multiplexList, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/admin/removeAllotmentForMovie")
	public ResponseEntity<String> removeAllotmentForMovie(@RequestBody String movieId){
		String success = multiplexService.removeAllotment(movieId);
		if(success == null) {
			return new ResponseEntity<String>("Record not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(success, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 */
	@ExceptionHandler(NoRecordFoundException.class)
	public ResponseEntity<MultiplexExceptionDto> exceptionHandler(NoRecordFoundException ex) {
		MultiplexExceptionDto exDto = new MultiplexExceptionDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<MultiplexExceptionDto>(exDto,HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 
	 * @return
	 */
	@ExceptionHandler(DuplicateRecordFoundExeption.class)
	public ResponseEntity<MultiplexExceptionDto> exceptionHandler(DuplicateRecordFoundExeption ex) {
		MultiplexExceptionDto exDto = new MultiplexExceptionDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<MultiplexExceptionDto>(exDto,HttpStatus.BAD_REQUEST);
	}
	

}
