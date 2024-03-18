package com.airlinereservation.dto;

import java.util.ArrayList;
import java.util.List;

import com.airlinereservation.entity.Flight;
import com.airlinereservation.entity.FlightBooking;

import lombok.Data;

@Data
public class FlightResponseDto extends CommonApiResponse {
	
	private List<Flight> flights = new ArrayList<>();
	
	private List<FlightBooking> flightBookings = new ArrayList<>();

}
