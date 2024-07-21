package com.movie.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.movie.entity.Booking;
import com.movie.entity.Hall;
import com.movie.entity.Movie;
import com.movie.entity.Seat;
import com.movie.entity.ShowSeat;
import com.movie.entity.ShowSeatId;
import com.movie.entity.Shows;
import com.movie.entity.Theater;
import com.movie.entity.User;
import com.movie.model.BookingInfo;
import com.movie.model.HallDTO;
import com.movie.model.MovieDTO;
import com.movie.model.SeatDto;
import com.movie.model.ShowDTO;
import com.movie.model.ShowInfo;
import com.movie.model.TheaterDTO;
import com.movie.repository.BookingRepo;
import com.movie.repository.HallRepo;
import com.movie.repository.MovieRepository;
import com.movie.repository.SeatRepo;
import com.movie.repository.ShowSeatRepo;
import com.movie.repository.ShowsRepo;
import com.movie.repository.TheaterRepo;
import com.movie.repository.UserRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class MovieServiceUser {
	@Autowired
	private MovieRepository movieRepo;

	@Autowired
	private TheaterRepo theaterRepo;

	@Autowired
	private MovieService movieService;

	@Autowired
	private ShowsRepo showRepo;
	
	@Autowired
	private SeatRepo seatRepo;
	
	@Autowired
	private ShowSeatRepo showSeatRepo;
	
	@Autowired
	private UserRepo urepo;
	
	@Autowired
	private HallRepo hallRepo;
	
	@Autowired
	private BookingRepo bokingRepo; 

	public List<MovieDTO> getMovie(String country) {
		List<Movie> allMovie = movieRepo.findByCountry(country);
		List<MovieDTO> dtos = new ArrayList<>();
		for (Movie movie : allMovie) {
			MovieDTO dto = createMovieDto(movie);
			dtos.add(dto);
		}
		return dtos;
	}

	public List<TheaterDTO> getTheaters(String location) {
		return movieService.getTheaters(location);
	}

	public List<ShowDTO> getShows(String movieName, String location) {
		List<Movie> findByTitle = movieRepo.findByTitle(movieName);
		List<Theater> theaters = theaterRepo.findByLocation(location);
		List<ShowDTO> dto = new ArrayList<>();
		for (Theater theater : theaters) {
			List<Hall> halls = theater.getHalls();
			for (Hall hall : halls) {
				List<Shows> shows = hall.getShows();

				for (Shows show : shows) {
					if (show.getCurrentMovie().getTitle().equals(findByTitle.get(0).getTitle())) {
						ShowDTO showdto = ShowDTO.builder().movie(createMovieDto(findByTitle.get(0)))
								.theater(createrTheaterDTO(hall,theater)).showid(show.getId()).time(show.getTime()).build();
						dto.add(showdto);
					}
				}

			}
		}
		return dto;
	}

	private TheaterDTO createrTheaterDTO(Hall hall,Theater theater) {
		List<HallDTO> hallDtos = new ArrayList<>();
		
			HallDTO hallDto = HallDTO.builder().id(hall.getId()).name(hall.getName()).totalSeats(hall.getTotalSeats())
					.build();
			hallDtos.add(hallDto);
		
		return TheaterDTO.builder().location(theater.getLocation()).name(theater.getName()).id(theater.getId())
				.halls(hallDtos).build();
	}

	private HallDTO createHallDTO(Hall hall) {
		return HallDTO.builder().id(hall.getId()).name(hall.getName()).totalSeats(hall.getTotalSeats()).build();
	}

	private MovieDTO createMovieDto(Movie movie) {
		return MovieDTO.builder().country(movie.getCountry()).title(movie.getTitle())
				.releaseDate(movie.getReleaseDate()).description(movie.getDescription())
				.durationInMins(movie.getDurationInMins()).build();
	}

	public ShowInfo getShowInfo(String showid) {
		Shows shows = showRepo.findById(Integer.parseInt(showid)).get();
		Hall hall = shows.getHall();
		Theater theater = hall.getTheater();
		List<SeatDto> seatDto = new ArrayList<>();
		for(ShowSeat showSeat : shows.getShowSeats()) {
			SeatDto dto = SeatDto.builder().seatNumber(showSeat.getSeat().getSeatNumber()).seatId(showSeat.getSeat().getId()).isAvailable(!showSeat.isIsbooked()).build();
			seatDto.add(dto);
		}
		Movie currentMovie = shows.getCurrentMovie();
		return ShowInfo.builder().movieName(currentMovie.getTitle()).duration(currentMovie.getDurationInMins() + " mins")
				.hallName(hall.getName()).number_of_seats(hall.getTotalSeats()).seats(seatDto).theaterName(theater.getName()).build();
	}

	public BookingInfo bookSeat(HttpSession httpSession, String showid, String seatid) {
		String user = httpSession.getAttribute("user").toString();
		Optional<Shows> show = showRepo.findById(Integer.parseInt(showid));
		Optional<Seat> seat = seatRepo.findById(Integer.parseInt(seatid));
		if(!show.isPresent()) {
			throw new RuntimeException("Show Does not exists !!");
		}
		if(!seat.isPresent()) {
			throw new RuntimeException("Seat Does not exists !!");
		}
		
		ShowSeatId showSeatId = new ShowSeatId();
		showSeatId.setSeatid(seat.get().getId());
		showSeatId.setShowid(show.get().getId());
		
		Optional<ShowSeat> showSeat = showSeatRepo.findById(showSeatId);
		if(showSeat.isPresent()) {
			boolean isbooked = showSeat.get().isIsbooked();
			if(isbooked) {
				throw new RuntimeException("Already Booked by Other People !!");
			}
			showSeat.get().setIsbooked(true);
		}
		
		Booking booking = new Booking();
		booking.setUser(urepo.findByEmail(user));
		booking.setMovieName(show.get().getCurrentMovie().getTitle());
		booking.setDateTime(LocalDateTime.now());
		booking.setSeatNo(seat.get().getSeatNumber()+"");
		booking.setReferenceNumber((seatid+showid+""+booking.hashCode()));
		booking.setTheaterName(show.get().getHall().getTheater().getName());
		
		booking = bokingRepo.save(booking);
		showSeatRepo.save(showSeat.get());
		
		
		return BookingInfo.builder().bookingId(booking.getReferenceNumber()).userid(user).date(booking.getDateTime().toString())
				.seatNo(booking.getSeatNo()).theaterName(booking.getTheaterName()).movieName(booking.getMovieName())
				.build();
	}

	public List<BookingInfo> getBookings(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String email = session.getAttribute("user").toString();
		User findByEmail = urepo.findByEmail(email);
		List<Booking> bookings =  bokingRepo.findByUser(findByEmail);
		
		List<BookingInfo> binfoList = new ArrayList<>();
		for(Booking booking : bookings) {
			BookingInfo binfo = BookingInfo.builder().bookingId(booking.getId().toString()).date(booking.getDateTime().toString())
					.movieName(booking.getMovieName()).theaterName(booking.getTheaterName()).seatNo(booking.getSeatNo()).userid(booking.getUser().getEmail()).refrenceId(booking.getReferenceNumber()).build();
			binfoList.add(binfo);
		}
		return binfoList;
	}

}
