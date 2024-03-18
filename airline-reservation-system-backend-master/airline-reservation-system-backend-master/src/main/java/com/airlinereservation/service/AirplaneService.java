package com.airlinereservation.service;

import java.util.List;

import com.airlinereservation.entity.Airplane;

public interface AirplaneService {
	
	Airplane add(Airplane airplane);
	Airplane getById(int id);
	List<Airplane> getAll();
	
}
