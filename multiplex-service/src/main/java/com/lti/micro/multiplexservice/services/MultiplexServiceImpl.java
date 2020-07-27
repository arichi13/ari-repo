package com.lti.micro.multiplexservice.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.lti.micro.multiplexservice.document.Multiplex;
import com.lti.micro.multiplexservice.document.Screen;
import com.lti.micro.multiplexservice.dto.MultiplexDto;
import com.lti.micro.multiplexservice.repositories.MultiplexRepository;
import com.lti.micro.multiplexservice.repositories.ScreenRepository;
import com.mongodb.client.result.UpdateResult;

/**
 * Implementation class for all service methods  
 *
 */
@Service
public class MultiplexServiceImpl implements IMultiplexService {
	
	private static final String SCREEN_CONST = "Screen";

	@Autowired
	MultiplexRepository multiplexRepo;
	
	@Autowired
	ScreenRepository screenRepo;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MultiplexDto> getAllMultiplexes() {
		Iterable<Multiplex> allMovies = multiplexRepo.findAll();
		ArrayList<MultiplexDto> listToReturn = new ArrayList<MultiplexDto>();
		allMovies.forEach((multiplex) ->
		{
			List<Screen> screens = screenRepo.findByMultiplexId(multiplex.getMultiplexId());
			List<String> screenNames = screens.stream().map(
					(screen) -> {
						return screen.getScreenName();	
					}).collect(Collectors.toList());
			listToReturn.add(new MultiplexDto(multiplex.getMultiplexId(), multiplex.getMultiplexName(), multiplex.getNumberOfScreens(), multiplex.getCity(), screenNames));
		});
		return listToReturn;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MultiplexDto> getMultiplexStartingByName(String name) {
		List<Multiplex> returnedMovies = multiplexRepo.findByMultiplexNameStartingWith(name);
		ArrayList<MultiplexDto> listToReturn = new ArrayList<MultiplexDto>();
		returnedMovies.forEach((multiplex) -> {
			listToReturn.add(new MultiplexDto(multiplex.getMultiplexId(), multiplex.getMultiplexName(), multiplex.getNumberOfScreens(), multiplex.getCity(), null));
		});	
		return listToReturn;
	}
	
	@Override
	public MultiplexDto getMultiplexById(String multiplexId) {
		Optional<Multiplex> returnedMultiplex = multiplexRepo.findById(multiplexId);
		if(returnedMultiplex.isPresent()){
			Multiplex multiplex = returnedMultiplex.get();
			List<Screen> screens = screenRepo.findByMultiplexId(multiplex.getMultiplexId());
			List<String> screenNames = screens.stream().map(
					(screen) -> {
						return screen.getScreenName();	
					}).collect(Collectors.toList());
			return new MultiplexDto(multiplex.getMultiplexId(), multiplex.getMultiplexName(), multiplex.getNumberOfScreens(), multiplex.getCity(), screenNames);
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MultiplexDto> getMultiplexByName(String name) {
		List<Multiplex> returnedMultiplexes = multiplexRepo.findByMultiplexName(name);
		ArrayList<MultiplexDto> listToReturn = new ArrayList<MultiplexDto>();
		returnedMultiplexes.forEach((multiplex) -> {
			List<Screen> screens = screenRepo.findByMultiplexId(multiplex.getMultiplexId());
			List<String> screenNames = screens.stream().map(
					(screen) -> {
						return screen.getScreenName();	
					}).collect(Collectors.toList());
			listToReturn.add(new MultiplexDto(multiplex.getMultiplexId(), multiplex.getMultiplexName(), multiplex.getNumberOfScreens(), multiplex.getCity(), screenNames));
		});	
		return listToReturn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String removeMultiplex(String multiplexDto) {
		System.out.println("remove called");
		
			int deleted = screenRepo.deleteByMultiplexId(multiplexDto);
			if(deleted > 0) {
				multiplexRepo.deleteById(multiplexDto);
				return "Movie deleted successfully";
			}
			return null;

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateMultiplex(MultiplexDto multiplexDto) {
		Optional<Multiplex> foundMultiplex = multiplexRepo.findById(multiplexDto.getMultiplexId());
		if(foundMultiplex.isPresent()) {
			Multiplex multiplex = foundMultiplex.get();
			multiplex.setMultiplexName(multiplexDto.getMultiplexName());
			multiplex.setCity(multiplexDto.getCity());
			multiplex.setNumberOfScreens(multiplexDto.getScreenNames().size());
			multiplexRepo.save(multiplex);
			
			//Screen management
			List<Screen> existingScreensEntity = screenRepo.findByMultiplexId(multiplex.getMultiplexId());
			List<String> existingScreens = existingScreensEntity.stream().map(
					(screen) -> {
						return screen.getScreenName();	
					}).collect(Collectors.toList());
			existingScreens.forEach(System.out::println);
			List<String> newScreens = multiplexDto.getScreenNames();
			newScreens.forEach(System.out::println);
			Set<String> newSet = new HashSet<String>(newScreens);
			//-> add all new screens not already present in db
			newScreens.removeAll(existingScreens);
			newScreens.forEach(System.out::println);
			newScreens.forEach((screenName) ->{
				System.out.println("inside save" + screenName);
				screenRepo.save(new Screen(null, screenName, multiplexDto.getMultiplexId(), null));
			});
			
			//-> remove all screens not present in new list
			existingScreens.removeAll(newSet);
			existingScreens.forEach(System.out::println);
			existingScreens.forEach((screenName) -> {
				System.out.println("inside delete" + screenName);
				screenRepo.deleteByScreenNameAndMultiplexId(screenName, multiplexDto.getMultiplexId());
			});
			return 1;
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createMultiplex(MultiplexDto multiplexDto) {
		Optional<Multiplex> foundMultiplex = multiplexRepo.findByMultiplexNameAndCity(multiplexDto.getMultiplexName(), multiplexDto.getCity());
		
		if(!foundMultiplex.isPresent()){
			int defaultScreens = multiplexDto.getNumberOfScreens() ==0? 1 : multiplexDto.getNumberOfScreens() ;
			Multiplex insertedMultiplex = multiplexRepo.save(new Multiplex(null, multiplexDto.getMultiplexName(), defaultScreens, multiplexDto.getCity()));
			
			if(insertedMultiplex != null){
				IntStream.rangeClosed(1, insertedMultiplex.getNumberOfScreens()).forEachOrdered((i) -> {
					Screen screenToSave = new Screen(null, SCREEN_CONST+i, insertedMultiplex.getMultiplexId(), null);
					screenRepo.save(screenToSave);
				});
				return insertedMultiplex.getMultiplexName();
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int allotMovieToMultiplex(String movieId, String multiplexId, String screenName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("screenName").is(screenName)).addCriteria(Criteria.where("multiplexId").is(multiplexId));;
		Update update = new Update();
		update.set("movieId", movieId);
		Screen screen = mongoTemplate.findAndModify(query, update, Screen.class);
		return 1;
		/*Optional<Screen> foundScreen = screenRepo.findByMultiplexIdAndScreenName(multiplexId, screenName);
		if(foundScreen.isPresent()){
			Optional<Screen> screen = screenRepo.findById(foundScreen.get().getScreenId());
					screen.get().setMovieId(movieId);
					screenRepo.save(screen.get());
			
		}
		
		return 0;*/
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAvailableScreens(String multiplexId) {
		ArrayList<String> listToReturn = new ArrayList<String>();
		List<Screen> screensList = screenRepo.findByMultiplexIdAndMovieIdNull(multiplexId);
		screensList.forEach((stream) -> listToReturn.add(stream.getScreenName()));
		return listToReturn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MultiplexDto> getMultiplexForMovie(String movieId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("movieId").is(movieId)).fields().include("multiplexId");
		
		List<Screen> screens = mongoTemplate.find(query, Screen.class);
		List<String> multiplexIds = screens.stream().map((screen)-> screen.getMultiplexId()).collect(Collectors.toList());
		Iterable<Multiplex> multiplexes = multiplexRepo.findAllById(multiplexIds);
		List<MultiplexDto> multiplexNames = new ArrayList<>();
		multiplexes.forEach((multiplex) ->{
			multiplexNames.add(new MultiplexDto(multiplex.getMultiplexId(), multiplex.getMultiplexName(), multiplex.getNumberOfScreens(), multiplex.getCity(), null));
		});
		return multiplexNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String removeAllotment(String movieId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("movieId").is(movieId));
		Update update = new Update();
		update.set("movieId", null);
		UpdateResult screen = mongoTemplate.updateMulti(query, update, Screen.class);
		
		
		//int recordDeleted = screenRepo.deleteByMovieId(movieId);
		return "Records deleted";
	}
	
}
