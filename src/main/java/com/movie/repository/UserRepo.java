package com.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	User findByEmail(String email);

}
