package com.movie.model;

import java.util.List;
import com.movie.entity.Hall;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class TheaterDTO {
	private Integer id;
	private String name;
	private String location;
	private List<HallDTO> halls;
}
