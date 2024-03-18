package com.airlinereservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airlinereservation.dto.AirportResponseDto;
import com.airlinereservation.dto.CommonApiResponse;
import com.airlinereservation.entity.Airport;
import com.airlinereservation.resource.AirportResource;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/airport/")
@CrossOrigin(origins = "http://localhost:3000")
public class AirportController {
	
	@Autowired
	private AirportResource airportResource;
	
	@PostMapping("add")
	@Operation(summary = "Api to add the Airport")
	public ResponseEntity<CommonApiResponse> addAirport(@RequestBody Airport airport) {
		return this.airportResource.addAirport(airport);
	}
	
	@GetMapping("/fetch/all")
	@Operation(summary = "Api to fetch all airports")
	public ResponseEntity<AirportResponseDto> fetchAllAirports() {
		return airportResource.fetchAllAirports();
	}

}
