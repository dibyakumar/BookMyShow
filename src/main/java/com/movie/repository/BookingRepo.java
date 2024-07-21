package com.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.entity.Booking;
import com.movie.entity.User;

public interface BookingRepo extends JpaRepository<Booking, Integer>{

	List<Booking> findByUser(User findByEmail);

}
