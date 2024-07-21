package com.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.entity.Theater;

public interface TheaterRepo extends JpaRepository<Theater, Integer>{

	List<Theater> findByLocation(String location);

}
