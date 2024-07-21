package com.movie.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String referenceNumber;
	private String seatNo;
	private String movieName;
	private String theaterName;
	private LocalDateTime dateTime;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
