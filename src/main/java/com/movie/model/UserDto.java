package com.movie.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
	private String name;
	private String password;
	private String email;
}
