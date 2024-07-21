package com.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.entity.ShowSeat;
import com.movie.entity.ShowSeatId;

public interface ShowSeatRepo extends JpaRepository<ShowSeat, ShowSeatId>{

}
