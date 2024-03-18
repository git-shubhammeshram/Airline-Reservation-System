package com.airlinereservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airlinereservation.dto.CommonApiResponse;
import com.airlinereservation.dto.FlightBookingRequestDto;
import com.airlinereservation.dto.FlightBookingResponseDto;
import com.airlinereservation.dto.FlightSeatDetailsResponse;
import com.airlinereservation.resource.FlightBookingResource;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/flight/book")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightBookingController {

	@Autowired
	private FlightBookingResource flightBookingResource;

	@PostMapping("add")
	@Operation(summary = "Api to add the Flight Booking")
	public ResponseEntity<CommonApiResponse> addAirport(@RequestBody FlightBookingRequestDto request) {
		return this.flightBookingResource.addFlightBooking(request);
	}

	@GetMapping("/fetch/all")
	@Operation(summary = "Api to fetch all flight bookings")
	public ResponseEntity<FlightBookingResponseDto> fetchAllFlights() {
		return this.flightBookingResource.fetchAllFlightBookings();
	}

	@GetMapping("/fetch")
	@Operation(summary = "Api to fetch flight bookings by flight id")
	public ResponseEntity<FlightBookingResponseDto> fetchAllByFlight(@RequestParam("flightId") int flightId) {
		return this.flightBookingResource.fetchBookingsByFlight(flightId);
	}

	@GetMapping("/fetch/user")
	@Operation(summary = "Api to fetch user flight bookings")
	public ResponseEntity<FlightBookingResponseDto> fetchUserBookings(@RequestParam("userId") int userId) {
		return this.flightBookingResource.fetchUserBookings(userId);
	}

	@DeleteMapping("/ticket/cancel")
	@Operation(summary = "Api to cancel the ticket using booking Id")
	public ResponseEntity<CommonApiResponse> cancelTrainTicket(@RequestParam("bookingId") int bookingId) {
		return this.flightBookingResource.cancelFlightBooking(bookingId);
	}

	@GetMapping("/download/ticket")
	@Operation(summary = "Api to download the ticket by Booking Ticket")
	public void downloadBill(@RequestParam("bookingId") String bookingId, HttpServletResponse response)
			throws Exception {
		this.flightBookingResource.downloadBookingTicket(bookingId, response);
	}
	
	@GetMapping("/fetch/seatDetails")
	@Operation(summary = "Api to fetch flight seat details")
	public ResponseEntity<FlightSeatDetailsResponse> fetchFlightSeatDetails(@RequestParam("flightId") int flightId) {
		return this.flightBookingResource.fetchFlightSeatDetails(flightId);
	}

}