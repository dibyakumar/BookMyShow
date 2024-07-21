package com.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.entity.Shows;

public interface ShowsRepo extends JpaRepository<Shows, Integer>{

}
