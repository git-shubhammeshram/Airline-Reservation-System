package com.airlinereservation.service;

import java.util.List;

import com.airlinereservation.entity.Airport;

public interface AirportService {
	
	Airport add(Airport airport);
	Airport getById(int id);
	List<Airport> getAll();

}
