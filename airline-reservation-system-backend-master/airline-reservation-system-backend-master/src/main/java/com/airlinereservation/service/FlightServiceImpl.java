package com.airlinereservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airlinereservation.dao.FlightDao;
import com.airlinereservation.entity.Airport;
import com.airlinereservation.entity.Flight;

@Service
public class FlightServiceImpl implements FlightService {
	
	@Autowired
	private FlightDao flightDao;

	@Override
	public Flight add(Flight flight) {
		// TODO Auto-generated method stub
		return flightDao.save(flight);
	}

	@Override
	public Flight getById(int id) {
		// TODO Auto-generated method stub
		return flightDao.findById(id).get();
	}

	@Override
	public List<Flight> getAll() {
		// TODO Auto-generated method stub
		return flightDao.findAll();
	}

	@Override
	public List<Flight> getByDepartureAirportAndArrivalAirportAndDepartureTimeBetweenAndStatusNotIn(
			Airport departureAirport, Airport arrivalAirport, String startTime, String endTime, List<String> status) {
		// TODO Auto-generated method stub
		return flightDao.findByDepartureAirportAndArrivalAirportAndDepartureTimeBetweenAndStatusNotIn(departureAirport, arrivalAirport, startTime, endTime, status);
	}

	@Override
	public List<Flight> getByDepartureTimeGreaterThanEqualAndStatusNotIn(String departureTime, List<String> status) {
		// TODO Auto-generated method stub
		return flightDao.findByDepartureTimeGreaterThanEqualAndStatusNotIn(departureTime, status);
	}

	

}
