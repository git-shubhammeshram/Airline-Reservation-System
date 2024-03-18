package com.airlinereservation.resource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.airlinereservation.dto.AirportResponseDto;
import com.airlinereservation.dto.CommonApiResponse;
import com.airlinereservation.entity.Airport;
import com.airlinereservation.service.AirportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AirportResource {

	private final Logger LOG = LoggerFactory.getLogger(AirportResource.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private AirportService airportService;

	public ResponseEntity<CommonApiResponse> addAirport(Airport airport) {

		LOG.info("Received request for add airport");

		CommonApiResponse response = new CommonApiResponse();

		if (airport == null) {
			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Airport addedAirport = this.airportService.add(airport);

		if (addedAirport == null) {
			response.setResponseMessage("Failed to add the Airport");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		response.setResponseMessage("Airport added successful");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<AirportResponseDto> fetchAllAirports() {

		AirportResponseDto response = new AirportResponseDto();

		List<Airport> airports = new ArrayList<>();

		airports = this.airportService.getAll();

		if (CollectionUtils.isEmpty(airports)) {
			response.setResponseMessage("Airport fetched success");
			response.setSuccess(true);

			return new ResponseEntity<AirportResponseDto>(response, HttpStatus.OK);
		}

		response.setAirports(airports);
		response.setResponseMessage("Airport fetched success");
		response.setSuccess(true);

		return new ResponseEntity<AirportResponseDto>(response, HttpStatus.OK);

	}

}
