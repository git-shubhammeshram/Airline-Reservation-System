package com.airlinereservation.dto;

import java.util.ArrayList;
import java.util.List;

import com.airlinereservation.entity.Airport;

import lombok.Data;

@Data
public class AirportResponseDto extends CommonApiResponse {

	private List<Airport> airports = new ArrayList<>();
	
}
