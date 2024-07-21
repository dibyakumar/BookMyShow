package com.movie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.model.MovieDTO;
import com.movie.model.ShowDTO;
import com.movie.model.TheaterDTO;
import com.movie.service.MovieService;

@RestController
@RequestMapping("/operator")
public class OperatorController {

	@Autowired
	private MovieService movieService;

	@PostMapping("/addMovie")
	public ResponseEntity<String> addMovie(@RequestBody MovieDTO movie) {
		return new ResponseEntity<>(movieService.saveMovie(movie), HttpStatus.OK);
	}

	@PostMapping("/addTheater")
	public ResponseEntity<String> addTheater(@RequestBody TheaterDTO theater) {
		return new ResponseEntity<>(movieService.saveTheater(theater), HttpStatus.OK);
	}

	@PostMapping("/addShow")
	public ResponseEntity<String> addShow(@RequestBody ShowDTO show) {
		return new ResponseEntity<>(movieService.saveShow(show), HttpStatus.OK);
	}

	@GetMapping("/getMovies")
	public ResponseEntity<List<MovieDTO>> getMovie(@RequestParam String title) {
		return new ResponseEntity<>(movieService.getMovie(title), HttpStatus.OK);
	}

	@GetMapping("/getTheaters")
	public ResponseEntity<List<TheaterDTO>> getTheaters(@RequestParam String location) {
		return new ResponseEntity<>(movieService.getTheaters(location), HttpStatus.OK);
	}
	
	@GetMapping("/getShowsByTheater")
	public ResponseEntity<List<ShowDTO>> getShows(@RequestParam("theaterid") String theaterid){
		return new ResponseEntity<List<ShowDTO>>(movieService.getShows(theaterid),HttpStatus.OK);
	}

	@DeleteMapping("/clear")
	public ResponseEntity<String> clearAll() {
		return new ResponseEntity<String>(movieService.clearAll(), HttpStatus.OK);
	}
	
	@PutMapping("/addRemoveShows")
	public ResponseEntity<String> addRemoveShowsShow(@RequestBody ShowDTO showDto ,@RequestParam(value = "showid",required = false) String showId,@RequestParam("type") String type){
		return new ResponseEntity<String>(movieService.updateShows(showId,showDto,type),HttpStatus.OK);
	}
}
