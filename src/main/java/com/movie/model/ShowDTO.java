package com.movie.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowDTO {
	private LocalDateTime time;
	private Integer hallId;
	private Integer movieId; 
	private Integer showid;
	private TheaterDTO theater;
	private MovieDTO movie;
	
}
