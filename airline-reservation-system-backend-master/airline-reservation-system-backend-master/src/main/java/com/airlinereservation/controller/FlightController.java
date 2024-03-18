package com.airlinereservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airlinereservation.dto.CommonApiResponse;
import com.airlinereservation.dto.FlightRequestDto;
import com.airlinereservation.dto.FlightResponseDto;
import com.airlinereservation.dto.FlightUpdateStatusRequestDto;
import com.airlinereservation.resource.FlightResource;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/flight/")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightController {
	
	@Autowired
	private FlightResource flightResource;
	
	@PostMapping("add")
	@Operation(summary = "Api to add the Flight")
	public ResponseEntity<CommonApiResponse> addFlight(@RequestBody FlightRequestDto flightRequest) {
		return this.flightResource.addFlight(flightRequest);
	}
	
	@GetMapping("/fetch/all")
	@Operation(summary = "Api to fetch all flights")
	public ResponseEntity<FlightResponseDto> fetchAllFlights() {
		return flightResource.fetchAllFlights();
	}
	
	@GetMapping("/search")
	@Operation(summary = "Api to search the flights")
	public ResponseEntity<FlightResponseDto> searchFlights(@RequestParam("fromAirportId") int fromAirportId, @RequestParam("endAirportId") int endAirportId
			,@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
		return flightResource.fetchAllFlightsByTimeRange(fromAirportId, endAirportId ,startTime, endTime);
	}
	
	@GetMapping("/status/all")
	@Operation(summary = "Api to fetch all flight status")
	public ResponseEntity<List<String>> fetchFlights() {
		return flightResource.fetchAllFlightStatus();
	}
	
	@GetMapping("/class/all")
	@Operation(summary = "Api to fetch all flight class")
	public ResponseEntity<List<String>> fetchFlightClass() {
		return flightResource.fetchAllFlightClass();
	}
	
	@PostMapping("update/status")
	@Operation(summary = "Api to update the Flight status")
	public ResponseEntity<CommonApiResponse> updateFlightStatus(@RequestBody FlightUpdateStatusRequestDto request) {
		return this.flightResource.updateFlightStatus(request);
	}
}
