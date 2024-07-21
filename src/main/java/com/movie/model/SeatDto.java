package com.movie.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatDto {
	private Integer seatId;
	private Integer seatNumber;
	private boolean isAvailable;
}
