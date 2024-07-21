package com.movie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.entity.Hall;
import com.movie.entity.Movie;
import com.movie.entity.Seat;
import com.movie.entity.ShowSeat;
import com.movie.entity.ShowSeatId;
import com.movie.entity.Shows;
import com.movie.entity.Theater;
import com.movie.model.HallDTO;
import com.movie.model.MovieDTO;
import com.movie.model.ShowDTO;
import com.movie.model.TheaterDTO;
import com.movie.repository.HallRepo;
import com.movie.repository.MovieRepository;
import com.movie.repository.SeatRepo;
import com.movie.repository.ShowSeatRepo;
import com.movie.repository.ShowsRepo;
import com.movie.repository.TheaterRepo;
import com.movie.repository.UserRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class MovieService {
	public static final String SAVED = "Saved !!";
	public static final String UPDATE = "UPDATE";
	public static final String REMOVE = "REMOVE";
	public static final String ADD = "ADD";

	@Autowired
	private MovieRepository movieRepo;
	@Autowired
	private TheaterRepo theaterRepo;
	@Autowired
	private HallRepo hallRepo;
	@Autowired
	private ShowsRepo showRepo;
	@Autowired
	private SeatRepo seatRepo;
	@Autowired
	private ShowSeatRepo showSeatRepo;
	@Autowired
	private UserRepo urepo;

	public String saveMovie(MovieDTO movie) {
		Movie moviedb = new Movie();
		moviedb.setDescription(movie.getDescription());
		moviedb.setTitle(movie.getTitle().toLowerCase());
		moviedb.setReleaseDate(movie.getReleaseDate());
		moviedb.setDurationInMins(movie.getDurationInMins());
		moviedb.setCountry(movie.getCountry().toLowerCase());
		movieRepo.save(moviedb);
		return SAVED;
	}

	public String saveTheater(TheaterDTO theater) {
		Theater theaterDB = new Theater();
		theaterDB.setName(theater.getName().toLowerCase());
		theaterDB.setLocation(theater.getLocation().toLowerCase());
		List<Hall> listHall = new ArrayList<>();
		for (HallDTO dto : theater.getHalls()) {
			Hall hall = new Hall();
			hall.setName(dto.getName());
			hall.setTotalSeats(dto.getTotalSeats());
			listHall.add(hall);
		}
		theaterDB.setHalls(listHall);
		for (Hall hall : theaterDB.getHalls()) {
			hall.setTheater(theaterDB);
		}
		theaterRepo.save(theaterDB);
		return SAVED;
	}


	public String saveShow(ShowDTO show) {
		Movie findCurrentMovie = findCurrentMovie(show.getMovieId());
		Hall hall = findCurrentHall(show.getHallId());
		Shows shows = new Shows();
		shows.setCurrentMovie(findCurrentMovie);
		shows.setTime(show.getTime());
		List<Shows> list;
		if (hall.getShows() == null) {
			list = new ArrayList<>();
			list.add(shows);
			hall.setShows(list);
		} else {
			list = hall.getShows();
			list.add(shows);
		}
		shows.setHall(hall);
		Shows savedShow = showRepo.save(shows);
		updateShowSeat(savedShow);
		return SAVED;
	}

	
	@Transactional
	private void updateShowSeat(Shows show) {
		   Hall hall = show.getHall();
		    int totalSeats = hall.getTotalSeats();

		    for (int i = 1; i <= totalSeats; i++) {
		        // Check if the seat already exists to avoid duplicates
		        Seat seat = seatRepo.findBySeatNumber(i);
		        if (seat == null) {
		            seat = new Seat();
		            seat.setSeatNumber(i);
		            seat = seatRepo.save(seat);
		        }

		        ShowSeatId showSeatId = new ShowSeatId(seat.getId(), show.getId());
		        ShowSeat showSeat = showSeatRepo.findById(showSeatId).orElse(null);

		        if (showSeat == null) {
		            showSeat = new ShowSeat();
		            showSeat.setId(showSeatId);
		            showSeat.setIsbooked(false);
		            showSeat.setSeat(seat);
		            showSeat.setShow(show);

		            // Add showSeat to the seat's showSeats collection
		            seat.getShowSeats().add(showSeat);

		            // Add showSeat to the show's showSeats collection
		            show.getShowSeats().add(showSeat);
		            
		            showSeatRepo.save(showSeat);
		          
		        }
		    }
	}
	private Hall findCurrentHall(int hallId) {
		Optional<Hall> findById = hallRepo.findById(hallId);
		if (findById.isEmpty()) {
			throw new RuntimeException("No Hall Found For Hall Id : " + hallId);
		}
		return findById.get();
	}

	private Movie findCurrentMovie(int movieId) {
		Optional<Movie> findById = movieRepo.findById(movieId);
		if (findById.isEmpty()) {
			throw new RuntimeException("No Movie Found For Movie Id : " + movieId);
		}
		return findById.get();
	}

	public List<MovieDTO> getMovie(String title) {
		List<Movie> movies = movieRepo.findByTitle(title);

		List<MovieDTO> dtos = new ArrayList<>();
		for (Movie movie : movies) {
			MovieDTO dto = MovieDTO.builder().id(movie.getId()).country(movie.getCountry()).title(movie.getTitle())
					.description(movie.getDescription()).releaseDate(movie.getReleaseDate())
					.durationInMins(movie.getDurationInMins()).build();
			dtos.add(dto);
		}
		return dtos;
	}

	public List<TheaterDTO> getTheaters(String location) {
		List<Theater> theaters = theaterRepo.findByLocation(location.toLowerCase());
		List<TheaterDTO> dtos = new ArrayList<>();
		for (Theater theater : theaters) {
			List<HallDTO> hallDtos = new ArrayList<>();
			for (Hall hall : theater.getHalls()) {
				HallDTO hallDto = HallDTO.builder().id(hall.getId()).name(hall.getName())
						.totalSeats(hall.getTotalSeats()).build();
				hallDtos.add(hallDto);
			}
			TheaterDTO dto = TheaterDTO.builder().location(theater.getLocation()).name(theater.getName())
					.id(theater.getId()).halls(hallDtos).build();
			dtos.add(dto);
		}
		return dtos;
	}

	public String clearAll() {
		urepo.deleteAllInBatch();
		showSeatRepo.deleteAllInBatch();
		seatRepo.deleteAllInBatch();
		showRepo.deleteAllInBatch();
		hallRepo.deleteAllInBatch();
		theaterRepo.deleteAllInBatch();
		return "cleared";
	}

	public String updateShows(String showId, ShowDTO showDto, String type) {

		if (UPDATE.equalsIgnoreCase(type)) {
			Shows show = showRepo.findById(Integer.parseInt(showId)).get();
			if (0 != showDto.getHallId()) {
				Hall hall = hallRepo.findById(showDto.getHallId()).get();
				show.setHall(hall);
			}
			if (0 != showDto.getMovieId()) {
				Movie movie = movieRepo.findById(showDto.getMovieId()).get();
				show.setCurrentMovie(movie);
			}
			if (null != showDto.getTime()) {
				show.setTime(showDto.getTime());
			}

			showRepo.save(show);
		} else if (ADD.equalsIgnoreCase(type)) {
			saveShow(showDto);
		} else if (REMOVE.equalsIgnoreCase(type)) {
			showRepo.deleteById(Integer.parseInt(showId));
		}

		return "Show Updated";
	}

	public List<ShowDTO> getShows(String theaterid) {
		Theater theater = theaterRepo.findById(Integer.parseInt(theaterid)).get();
		List<Hall> halls = theater.getHalls();
		List<ShowDTO> dtos = new ArrayList<>();
		
		for(Hall hall : halls) {
			List<Shows> shows = hall.getShows();
			if(!shows.isEmpty()) {
				for(Shows show : shows){
				ShowDTO dto = ShowDTO.builder().movieId(show.getCurrentMovie().getId()).hallId(show.getHall().getId()).time(show.getTime()).showid(show.getId()).build();
				dtos.add(dto);
				}
			}
		}
		
		return dtos;
	}

}
