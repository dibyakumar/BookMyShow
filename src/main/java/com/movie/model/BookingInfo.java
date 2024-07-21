package com.movie.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingInfo {
	private String refrenceId;
	private String userid;
	private String bookingId;
	private String seatNo;
	private String movieName;
	private String theaterName;
	private String date;
}
