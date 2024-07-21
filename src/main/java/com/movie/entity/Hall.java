package com.movie.entity;

import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Hall {
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private Integer id;
	private String name;
	private int totalSeats;
	@ManyToOne
	@JoinColumn(name = "theater_id")
	private Theater theater;
	@OneToMany(mappedBy = "hall" , cascade = CascadeType.ALL)
	private List<Shows> shows;
	
}
