package com.airlinereservation.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String flightNumber;
	
	private String departureTime;
	
	private String arrivalTime;
	
	private String status; // Scheduled, On Time, Delayed, etc.

	@ManyToOne
	@JoinColumn(name = "departure_airport_id")
	private Airport departureAirport;

	@ManyToOne
	@JoinColumn(name = "arrival_airport_id")
	private Airport arrivalAirport;

	@ManyToOne
	@JoinColumn(name = "airplane_id")
	private Airplane airplane;
	
	private BigDecimal economySeatFare;
	
	private BigDecimal businessSeatFare;
	
	private BigDecimal firstClassSeatFare;
	
	
	// from Airplane Entity
    private int totalSeat;
    
    private int economySeats;
	
    private int businessSeats;
    
    private int firstClassSeats;
	

}
