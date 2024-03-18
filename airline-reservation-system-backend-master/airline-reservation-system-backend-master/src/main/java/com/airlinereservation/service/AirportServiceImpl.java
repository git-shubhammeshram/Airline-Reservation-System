package com.airlinereservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airlinereservation.dao.AirportDao;
import com.airlinereservation.entity.Airport;

@Service
public class AirportServiceImpl implements AirportService {

	@Autowired
	private AirportDao airportDao;
	
	@Override
	public Airport add(Airport airport) {
		// TODO Auto-generated method stub
		return airportDao.save(airport);
	}

	@Override
	public Airport getById(int id) {
		// TODO Auto-generated method stub
		return airportDao.findById(id).get();
	}

	@Override
	public List<Airport> getAll() {
		// TODO Auto-generated method stub
		return airportDao.findAll();
	}

}
