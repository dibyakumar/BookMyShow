package com.movie.model;

import java.time.LocalDate;
import java.util.List;

import com.movie.entity.Shows;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieDTO {
	private int id;
	private String title;
	private String description;
	private String durationInMins;
	private LocalDate releaseDate;
	private String country;
}
