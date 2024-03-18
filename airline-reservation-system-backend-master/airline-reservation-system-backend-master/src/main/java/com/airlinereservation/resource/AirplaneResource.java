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

import com.airlinereservation.dao.AirplaneSeatNoDao;
import com.airlinereservation.dto.AirplaneResponseDto;
import com.airlinereservation.dto.CommonApiResponse;
import com.airlinereservation.entity.Airplane;
import com.airlinereservation.entity.AirplaneSeatNo;
import com.airlinereservation.entity.FlightBooking;
import com.airlinereservation.service.AirplaneSeatNoService;
import com.airlinereservation.service.AirplaneService;
import com.airlinereservation.utility.IdGenerator;
import com.airlinereservation.utility.Constants.AirplaneStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AirplaneResource {
	
	private final Logger LOG = LoggerFactory.getLogger(AirportResource.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private AirplaneService airplaneService;
	
	@Autowired
	private AirplaneSeatNoService airplaneSeatNoService;

	public ResponseEntity<CommonApiResponse> addAirplane(Airplane airplane) {

		LOG.info("Received request for add airplane");

		CommonApiResponse response = new CommonApiResponse();

		if (airplane == null) {
			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		airplane.setStatus(AirplaneStatus.ACTIVE.value());

		Airplane addedAirplane = this.airplaneService.add(airplane);
		
		List<AirplaneSeatNo> airplaneSeats = new ArrayList<>();
		
		List<String> seats = IdGenerator.generateSeatNumbers(airplane.getEconomySeats(), airplane.getBusinessSeats(), airplane.getFirstClassSeats());

		for(String seat : seats) {
			AirplaneSeatNo seatNo = new AirplaneSeatNo();
			seatNo.setAirplane(addedAirplane);
			seatNo.setSeatNo(seat);
			airplaneSeats.add(seatNo);
		}
		
		airplaneSeatNoService.addAllSeat(airplaneSeats);

		if (addedAirplane == null) {
			response.setResponseMessage("Failed to add the airplane");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		response.setResponseMessage("Airplane added successful");
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

	public ResponseEntity<AirplaneResponseDto> fetchAllAirplane() {

		AirplaneResponseDto response = new AirplaneResponseDto();

		List<Airplane> airplanes = new ArrayList<>();

		airplanes = this.airplaneService.getAll();

		if (CollectionUtils.isEmpty(airplanes)) {
			response.setResponseMessage("Airplane fetched success");
			response.setSuccess(true);

			return new ResponseEntity<AirplaneResponseDto>(response, HttpStatus.OK);
		}

		response.setAirplanes(airplanes);
		response.setResponseMessage("Airplane fetched success");
		response.setSuccess(true);

		return new ResponseEntity<AirplaneResponseDto>(response, HttpStatus.OK);

	}

}
