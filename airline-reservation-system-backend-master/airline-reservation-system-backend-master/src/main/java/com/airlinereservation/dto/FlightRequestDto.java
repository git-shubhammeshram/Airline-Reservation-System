package com.airlinereservation.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FlightRequestDto {
	
	private String departureTime;
	
	private String arrivalTime;
	
	private String status; // Scheduled, On Time, Delayed, etc.

	private int departureAirportId;

	private int arrivalAirportId;

	private int airplaneId;
	
    private BigDecimal economySeatFare;
	
	private BigDecimal businessSeatFare;
	
	private BigDecimal firstClassSeatFare;

}
