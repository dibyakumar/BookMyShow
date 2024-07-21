package com.movie.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "show_seat")
public class ShowSeat {
	@EmbeddedId
	private ShowSeatId id;
	@ManyToOne
	@MapsId("showid")
	private Shows show;
	@ManyToOne
	@MapsId("seatid")
	private Seat seat;
		
	private boolean isbooked;
}
