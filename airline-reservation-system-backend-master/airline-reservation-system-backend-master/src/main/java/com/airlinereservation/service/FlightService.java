package com.airlinereservation.service;

import java.util.List;

import com.airlinereservation.entity.Airport;
import com.airlinereservation.entity.Flight;

public interface FlightService {

	Flight add(Flight flight);

	Flight getById(int id);

	List<Flight> getAll();

	List<Flight> getByDepartureAirportAndArrivalAirportAndDepartureTimeBetweenAndStatusNotIn(Airport departureAirport,
			Airport arrivalAirport, String startTime, String endTime, List<String> status);
	
	List<Flight> getByDepartureTimeGreaterThanEqualAndStatusNotIn(String departureTime, List<String> status);

}
