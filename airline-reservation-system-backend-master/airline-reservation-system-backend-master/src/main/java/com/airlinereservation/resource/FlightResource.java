package com.airlinereservation.resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.airlinereservation.dto.CommonApiResponse;
import com.airlinereservation.dto.FlightRequestDto;
import com.airlinereservation.dto.FlightResponseDto;
import com.airlinereservation.dto.FlightUpdateStatusRequestDto;
import com.airlinereservation.entity.Airplane;
import com.airlinereservation.entity.AirplaneSeatNo;
import com.airlinereservation.entity.Airport;
import com.airlinereservation.entity.Flight;
import com.airlinereservation.entity.FlightBooking;
import com.airlinereservation.service.AirplaneSeatNoService;
import com.airlinereservation.service.AirplaneService;
import com.airlinereservation.service.AirportService;
import com.airlinereservation.service.FlightBookingService;
import com.airlinereservation.service.FlightService;
import com.airlinereservation.utility.Constants.FlightBookingStatus;
import com.airlinereservation.utility.Constants.FlightClassType;
import com.airlinereservation.utility.Constants.FlightStatus;
import com.airlinereservation.utility.IdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FlightResource {
	
	private final Logger LOG = LoggerFactory.getLogger(FlightResource.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private FlightBookingService flightBookingService;
	
	@Autowired
	private AirportService airportService;
	
	@Autowired
	private AirplaneService airplaneService;
	
	@Autowired
	private AirplaneSeatNoService airplaneSeatNoService;

	public ResponseEntity<CommonApiResponse> addFlight(FlightRequestDto flightRequest) {

		LOG.info("Received request for add flight");

		CommonApiResponse response = new CommonApiResponse();

		if (flightRequest == null || flightRequest.getAirplaneId() == 0 
				|| flightRequest.getArrivalAirportId() == 0 | flightRequest.getDepartureAirportId() == 0) {
			
			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		Airport arrivalAirport = this.airportService.getById(flightRequest.getArrivalAirportId());
		Airport departureAirport = this.airportService.getById(flightRequest.getDepartureAirportId());
		
		Airplane airplane = this.airplaneService.getById(flightRequest.getAirplaneId());
		
		Flight flight = new Flight();
		flight.setArrivalTime(flightRequest.getArrivalTime());
		flight.setAirplane(airplane);
		flight.setArrivalAirport(arrivalAirport);
		flight.setDepartureAirport(departureAirport);
		flight.setDepartureTime(flightRequest.getDepartureTime());
		flight.setStatus(FlightStatus.SCHEDULED.value());
		flight.setTotalSeat(airplane.getTotalSeat());
		flight.setFirstClassSeats(airplane.getFirstClassSeats());
		flight.setEconomySeats(airplane.getEconomySeats());
		flight.setBusinessSeats(airplane.getBusinessSeats());
		flight.setFlightNumber(IdGenerator.generateFlightNumber());
		flight.setEconomySeatFare(flightRequest.getEconomySeatFare());
		flight.setBusinessSeatFare(flightRequest.getBusinessSeatFare());
		flight.setFirstClassSeatFare(flightRequest.getFirstClassSeatFare());

		Flight addedFlight = this.flightService.add(flight);

		List<AirplaneSeatNo> airplaneSeats = new ArrayList<>();
		
		airplaneSeats = airplaneSeatNoService.getByAirplane(airplane);
		
		for(AirplaneSeatNo airplaneSeat : airplaneSeats) {
			FlightBooking booking = new FlightBooking();
			booking.setAirplaneSeatNo(airplaneSeat);
			booking.setFlight(addedFlight);
			
			if(airplaneSeat.getSeatNo().contains("E-")) {
				booking.setFlightClass(FlightClassType.ECONOMY.value());
			} else if (airplaneSeat.getSeatNo().contains("B-")) {
				booking.setFlightClass(FlightClassType.BUSINESS.value());
			} else if (airplaneSeat.getSeatNo().contains("F-")) {
				booking.setFlightClass(FlightClassType.FIRST_CLASS.value());
			}
			
			booking.setStatus(FlightBookingStatus.AVAILABLE.value());
			
			flightBookingService.add(booking);
			
		}

		if (addedFlight == null) {
			response.setResponseMessage("Failed to add the Flight");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setResponseMessage("Flight added succcessfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<FlightResponseDto> fetchAllFlights() {

		FlightResponseDto response = new FlightResponseDto();
		
		String startTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<Flight> flights = new ArrayList<>();

		flights = this.flightService.getByDepartureTimeGreaterThanEqualAndStatusNotIn(String.valueOf(startTime), java.util.Arrays.asList(FlightStatus.CANCELED.value(),
				FlightStatus.COMPLETED.value()));

		if (CollectionUtils.isEmpty(flights)) {
			response.setResponseMessage("Flights fetched success");
			response.setSuccess(true);

			return new ResponseEntity<FlightResponseDto>(response, HttpStatus.OK);
		}
		
		response.setFlights(flights);
		
		response.setResponseMessage("Airport fetched success");
		response.setSuccess(true);

		return new ResponseEntity<FlightResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<FlightResponseDto> fetchAllFlightsByTimeRange(int fromAirportId, int toAirportId ,String startTime, String endTime) {

		FlightResponseDto response = new FlightResponseDto();
		
		if(startTime == null || endTime == null || fromAirportId ==0 || toAirportId == 0) {
			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<FlightResponseDto>(response, HttpStatus.BAD_REQUEST);	
		}
		
		Airport fromAirport = this.airportService.getById(fromAirportId);
		Airport toAirport = this.airportService.getById(toAirportId);

		List<Flight> flights = new ArrayList<>();
		
		java.util.Arrays.asList(FlightStatus.CANCELED.value());

		flights = this.flightService.getByDepartureAirportAndArrivalAirportAndDepartureTimeBetweenAndStatusNotIn(fromAirport, toAirport, 
				String.valueOf(startTime), String.valueOf(endTime) , java.util.Arrays.asList(FlightStatus.CANCELED.value(),
						FlightStatus.COMPLETED.value()));

		if (CollectionUtils.isEmpty(flights)) {
			response.setResponseMessage("Flights fetched success");
			response.setSuccess(true);

			return new ResponseEntity<FlightResponseDto>(response, HttpStatus.OK);
		}

		response.setFlights(flights);
		response.setResponseMessage("Airport fetched success");
		response.setSuccess(true);

		return new ResponseEntity<FlightResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<List<String>> fetchAllFlightStatus() {
		
		List<String> flightStatus = new ArrayList<>();
		
		for(FlightStatus status: FlightStatus.values()) {
			flightStatus.add(status.value());
		}
		
		return new ResponseEntity<List<String>>(flightStatus, HttpStatus.OK);
	}

	
    public ResponseEntity<List<String>> fetchAllFlightClass() {
		
		List<String> flightClass = new ArrayList<>();
		
		for(FlightClassType classType: FlightClassType.values()) {
			flightClass.add(classType.value());
		}
		
		return new ResponseEntity<List<String>>(flightClass, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> updateFlightStatus(FlightUpdateStatusRequestDto request) {
		
		LOG.info("Received request for update flight status");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		Flight flight = this.flightService.getById(request.getFlightId());
		flight.setStatus(request.getStatus());
		
	    this.flightService.add(flight);
	    
	    response.setResponseMessage("Flight status updated successful");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

}
