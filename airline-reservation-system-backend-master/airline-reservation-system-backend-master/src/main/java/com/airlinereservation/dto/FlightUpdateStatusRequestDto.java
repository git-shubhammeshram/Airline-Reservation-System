package com.airlinereservation.dto;

import lombok.Data;

@Data
public class FlightUpdateStatusRequestDto {
	
	private int flightId;
	
	private String status;

}
