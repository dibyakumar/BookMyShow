package com.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.entity.Seat;

public interface SeatRepo  extends JpaRepository<Seat, Integer>{

	Seat findBySeatNumber(int i);

}
