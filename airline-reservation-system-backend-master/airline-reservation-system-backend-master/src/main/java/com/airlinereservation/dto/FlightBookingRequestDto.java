package com.airlinereservation.dto;

import lombok.Data;

@Data
public class FlightBookingRequestDto {
	
    private int totalPassengers;
    
    private String flightClassType;
    
    private int passengerId;

    private int flightId;

}
