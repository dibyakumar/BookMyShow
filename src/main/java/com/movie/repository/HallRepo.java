package com.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.entity.Hall;

public interface HallRepo extends JpaRepository<Hall, Integer>{

}
