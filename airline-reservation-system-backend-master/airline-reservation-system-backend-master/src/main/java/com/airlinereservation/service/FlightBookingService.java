package com.airlinereservation.service;

import java.util.List;

import com.airlinereservation.entity.Flight;
import com.airlinereservation.entity.FlightBooking;
import com.airlinereservation.entity.User;

public interface FlightBookingService {
	
	FlightBooking add(FlightBooking flightBooking);
	FlightBooking getById(int id);
	List<FlightBooking> getAll();
	List<FlightBooking> getByPassenger(User user);
	List<FlightBooking> getByFlight(Flight flight);
	List<FlightBooking> getByFlightAndStatus(Flight flight, String status);
	List<FlightBooking> getByFlightAndStatusNotIn(Flight flight, List<String> status);
	List<FlightBooking> getByFlightAndStatusAndFlightClass(Flight flight, String status, String flightClass);
	List<FlightBooking> getByFlightAndStatusNotInAndFlightClass(Flight flight, List<String> status, String flightClass);
	List<FlightBooking> getByFlightAndFlightClass(Flight flight, String flightClass);
	List<FlightBooking> getByIdAndStatus(int id, String status);
	List<FlightBooking> getByStatusIn(List<String> statuses);
	List<FlightBooking> getByBookingId(String bookingId);


}
