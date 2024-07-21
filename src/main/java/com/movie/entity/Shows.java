package com.movie.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Shows {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private LocalDateTime time;
	@ManyToOne
	@JoinColumn(name = "hall_id")
	private Hall hall;
	@OneToMany(mappedBy = "show")
	private List<ShowSeat> showSeats = new ArrayList<>();
	@ManyToOne
	@JoinColumn(name = "movie_id")
	private Movie currentMovie;
}
