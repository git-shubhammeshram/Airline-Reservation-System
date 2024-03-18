package com.airlinereservation.dto;

import lombok.Data;

@Data
public class FlightSeatDetailsResponse extends CommonApiResponse {

	private int totalSeat;

	private int economySeats;

	private int businessSeats;

	private int firstClassSeats;

	private int economySeatsAvailable;

	private int businessSeatsAvailable;

	private int firstClassSeatsAvailable;
	
	private int economySeatsWaiting;

	private int businessSeatsWaiting;

	private int firstClassSeatsWaiting;

}
