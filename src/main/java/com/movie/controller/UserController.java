package com.movie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.model.BookingInfo;
import com.movie.model.MovieDTO;
import com.movie.model.ShowDTO;
import com.movie.model.ShowInfo;
import com.movie.model.TheaterDTO;
import com.movie.model.UserDto;
import com.movie.service.AuthenticationService;
import com.movie.service.MovieServiceUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {	

	@Autowired
	private MovieServiceUser movieService;
	@Autowired
	private AuthenticationService authService;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDto user){
		return new ResponseEntity<String>(authService.register(user),HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpServletRequest request,@RequestParam String email,@RequestParam String password){
		HttpSession session = request.getSession(true);
		return new ResponseEntity<String>(authService.login(session,email,password),HttpStatus.OK);
	}
	
	@PostMapping("logout")
	public ResponseEntity<String> logOut(HttpServletRequest request){
		return new ResponseEntity<String>(authService.logout(request),HttpStatus.OK);
	}
	
	@GetMapping("/getMovieByCountry")
	public ResponseEntity<List<MovieDTO>> getMovieByCountry(HttpServletRequest request,@RequestParam("country") String country){
		authService.check(request);
		return new ResponseEntity<List<MovieDTO>>(movieService.getMovie(country),HttpStatus.OK);
	}
	
	@GetMapping("/getTheatrsByLocation")
	public ResponseEntity<List<TheaterDTO>> getTheatersByLocation(HttpServletRequest request,@RequestParam("location") String location){
		authService.check(request);
		return new ResponseEntity<List<TheaterDTO>>(movieService.getTheaters(location),HttpStatus.OK);
	}
	
	@GetMapping("/getShows")
	public ResponseEntity<List<ShowDTO>> getShows(HttpServletRequest request,@RequestParam("moviename")String movieName,@RequestParam("location")String location){
		authService.check(request);
		return new ResponseEntity<List<ShowDTO>>(movieService.getShows(movieName,location),HttpStatus.OK);
	}
	
	@GetMapping("/showInfo")
	public ResponseEntity<ShowInfo> getShowInfo(HttpServletRequest request,@RequestParam("showid") String showid){
		authService.check(request);
		return new ResponseEntity<ShowInfo>(movieService.getShowInfo(showid),HttpStatus.OK);
	}
	
	@PostMapping("/bookSeat")
	public ResponseEntity<BookingInfo> bookSeat(HttpServletRequest request,@RequestParam String showid,@RequestParam String seatid){
		authService.check(request);
		return new ResponseEntity<BookingInfo>(movieService.bookSeat(request.getSession(false), showid,seatid),HttpStatus.OK);
	}
	
	@GetMapping("/bookings")
	public ResponseEntity<List<BookingInfo>> getBookings(HttpServletRequest request){
		authService.check(request);
		return new ResponseEntity<List<BookingInfo>>(movieService.getBookings(request),HttpStatus.OK);
	}
	
}
