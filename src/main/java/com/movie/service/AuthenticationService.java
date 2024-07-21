package com.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.entity.User;
import com.movie.model.UserDto;
import com.movie.repository.UserRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthenticationService {
	@Autowired
	private UserRepo userRepo;

	public String register(UserDto user) {
		User userEntity = new User();
		userEntity.setEmail(user.getEmail());
		userEntity.setName(user.getName());
		userEntity.setPassword(user.getPassword());
		userRepo.save(userEntity);
		return "UserAdded";
	}

	public String login(HttpSession session, String email, String password) {
		User user = userRepo.findByEmail(email);
		if(user == null) {
			return "Invalid/Unidentified Email";
		}
		if(!password.equals(password)) {
			return "Wrong Password";
		}
		session.setAttribute("user", email);
		return "Login SuccessFull";
	}

	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null)
			return "No Login found";
		String res = session.getAttribute("user").toString()+ " Logged Out !!";
		session.invalidate();
		return res;
	}

	public void check(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) throw new RuntimeException("Please Login");
	}
	
}
