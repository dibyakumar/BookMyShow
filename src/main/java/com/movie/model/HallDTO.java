package com.movie.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HallDTO {
	private int id;
	private String name;
	private int totalSeats;
}
