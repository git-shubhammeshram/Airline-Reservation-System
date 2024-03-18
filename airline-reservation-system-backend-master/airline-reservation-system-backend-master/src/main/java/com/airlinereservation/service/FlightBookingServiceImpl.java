package com.airlinereservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airlinereservation.dao.FlightBookingDao;
import com.airlinereservation.entity.Flight;
import com.airlinereservation.entity.FlightBooking;
import com.airlinereservation.entity.User;

@Service
public class FlightBookingServiceImpl implements FlightBookingService {
	
	@Autowired
	private FlightBookingDao flightBookingDao;

	@Override
	public FlightBooking add(FlightBooking flightBooking) {
		// TODO Auto-generated method stub
		return flightBookingDao.save(flightBooking);
	}

	@Override
	public FlightBooking getById(int id) {
		// TODO Auto-generated method stub
		return flightBookingDao.findById(id).get();
	}

	@Override
	public List<FlightBooking> getAll() {
		// TODO Auto-generated method stub
		return flightBookingDao.findAll();
	}

	@Override
	public List<FlightBooking> getByPassenger(User user) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByPassenger(user);
	}

	@Override
	public List<FlightBooking> getByFlight(Flight flight) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByFlight(flight);
	}

	@Override
	public List<FlightBooking> getByFlightAndStatus(Flight flight, String status) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByFlightAndStatus(flight, status);
	}

	@Override
	public List<FlightBooking> getByFlightAndStatusNotIn(Flight flight, List<String> status) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByFlightAndStatusNotIn(flight, status);
	}

	@Override
	public List<FlightBooking> getByFlightAndStatusAndFlightClass(Flight flight, String status, String flightClass) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByFlightAndStatusAndFlightClass(flight, status, flightClass);
	}

	@Override
	public List<FlightBooking> getByFlightAndStatusNotInAndFlightClass(Flight flight, List<String> status,
			String flightClass) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByFlightAndStatusNotInAndFlightClass(flight, status, flightClass);
	}

	@Override
	public List<FlightBooking> getByFlightAndFlightClass(Flight flight, String flightClass) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByFlightAndFlightClass(flight, flightClass);
	}

	@Override
	public List<FlightBooking> getByIdAndStatus(int id, String status) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByIdAndStatus(id, status);
	}

	@Override
	public List<FlightBooking> getByStatusIn(List<String> statuses) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByStatusIn(statuses);
	}

	@Override
	public List<FlightBooking> getByBookingId(String bookingId) {
		// TODO Auto-generated method stub
		return flightBookingDao.findByBookingId(bookingId);
	}

}
