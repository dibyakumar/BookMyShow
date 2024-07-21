package com.movie.model;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowInfo {
	private String movieName;
	private String theaterName;
	private String hallName;
	private String duration;
	private Integer number_of_seats;
	private List<SeatDto> seats;
}
