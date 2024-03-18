package com.airlinereservation.resource;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.airlinereservation.dto.CommonApiResponse;
import com.airlinereservation.dto.FlightBookingRequestDto;
import com.airlinereservation.dto.FlightBookingResponseDto;
import com.airlinereservation.dto.FlightSeatDetailsResponse;
import com.airlinereservation.entity.Flight;
import com.airlinereservation.entity.FlightBooking;
import com.airlinereservation.entity.User;
import com.airlinereservation.service.FlightBookingService;
import com.airlinereservation.service.FlightService;
import com.airlinereservation.service.UserService;
import com.airlinereservation.utility.Constants.FlightBookingStatus;
import com.airlinereservation.utility.Constants.FlightClassType;
import com.airlinereservation.utility.IdGenerator;
import com.airlinereservation.utility.TicketDownloader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class FlightBookingResource {

	private final Logger LOG = LoggerFactory.getLogger(FlightBookingResource.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private FlightBookingService flightBookingService;

	@Autowired
	private FlightService flightService;

	@Autowired
	private UserService userService;

	public ResponseEntity<CommonApiResponse> addFlightBooking(FlightBookingRequestDto request) {

		LOG.info("Received request for add flight");

		CommonApiResponse response = new CommonApiResponse();

		if (request.getFlightClassType() == null || request.getTotalPassengers() == 0
				|| request.getFlightId() == 0 | request.getPassengerId() == 0) {

			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Flight flight = this.flightService.getById(request.getFlightId());

		User passenger = this.userService.getUserById(request.getPassengerId());

		String bookingTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

//		List<FlightBooking> totalFlightBookings = this.flightBookingService.getByFlight(flight);

		List<FlightBooking> totalFlightBookings = new ArrayList<>();

		String bookingId = IdGenerator.generateBookingId();

		List<FlightBooking> totalAvailableBookings = new ArrayList<>();
		List<FlightBooking> totalWaitingBookings = new ArrayList<>();

		int totalAvailableSeat;
		int totalWaitingSeat;

		if (request.getFlightClassType().equals(FlightClassType.ECONOMY.value())) {

			BigDecimal totalFare = flight.getEconomySeatFare()
					.multiply(BigDecimal.valueOf(request.getTotalPassengers()));

			if (passenger.getWalletAmount().compareTo(totalFare) < 0) {
				response.setResponseMessage("Insufficient Funds in Passenger Wallet");
				response.setSuccess(true);

				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			}

			totalFlightBookings = this.flightBookingService.getByFlightAndFlightClass(flight,
					FlightClassType.ECONOMY.value());

		}

		else if (request.getFlightClassType().equals(FlightClassType.BUSINESS.value())) {

			BigDecimal totalFare = flight.getBusinessSeatFare()
					.multiply(BigDecimal.valueOf(request.getTotalPassengers()));

			if (passenger.getWalletAmount().compareTo(totalFare) < 0) {
				response.setResponseMessage("Insufficient Funds in Passenger Wallet");
				response.setSuccess(true);

				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			}

			totalFlightBookings = this.flightBookingService.getByFlightAndFlightClass(flight,
					FlightClassType.BUSINESS.value());

		}

		else if (request.getFlightClassType().equals(FlightClassType.FIRST_CLASS.value())) {

			BigDecimal totalFare = flight.getFirstClassSeatFare()
					.multiply(BigDecimal.valueOf(request.getTotalPassengers()));

			if (passenger.getWalletAmount().compareTo(totalFare) < 0) {
				response.setResponseMessage("Insufficient Funds in Passenger Wallet");
				response.setSuccess(true);

				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			}

			totalFlightBookings = this.flightBookingService.getByFlightAndFlightClass(flight,
					FlightClassType.FIRST_CLASS.value());

		}

		for (FlightBooking flightBooking : totalFlightBookings) {

			if (flightBooking.getStatus().equals(FlightBookingStatus.AVAILABLE.value())) {
				totalAvailableBookings.add(flightBooking);
				continue;
			}

			if (flightBooking.getStatus().equals(FlightBookingStatus.WAITING.value())) {
				totalWaitingBookings.add(flightBooking);
				continue;
			}

		}

		totalAvailableSeat = totalAvailableBookings.size();
		totalWaitingSeat = totalWaitingBookings.size();

		// all the seats are available
		if (request.getTotalPassengers() <= totalAvailableSeat) {

			for (int i = 0; i < request.getTotalPassengers(); i++) {
				FlightBooking booking = totalAvailableBookings.get(i);
				booking.setBookingId(bookingId);
				booking.setBookingTime(bookingTime);
				booking.setStatus(FlightBookingStatus.CONFIRMED.value());
				booking.setPassenger(passenger);
				this.flightBookingService.add(booking);

				if (request.getFlightClassType().equals(FlightClassType.ECONOMY.value())) {
					passenger.setWalletAmount(passenger.getWalletAmount().subtract(flight.getEconomySeatFare()));

				} else if (request.getFlightClassType().equals(FlightClassType.BUSINESS.value())) {
					passenger.setWalletAmount(passenger.getWalletAmount().subtract(flight.getBusinessSeatFare()));

				} else if (request.getFlightClassType().equals(FlightClassType.FIRST_CLASS.value())) {
					passenger.setWalletAmount(passenger.getWalletAmount().subtract(flight.getFirstClassSeatFare()));

				}

				this.userService.registerUser(passenger);

			}

		}

		// all seats are not available
		else {

			// that means already some users booking is in waiting, so this bookings will
			// also be in waiting no doubt
			if (totalWaitingSeat > 0) {

				for (int i = 0; i < request.getTotalPassengers(); i++) {
					FlightBooking existingBooking = totalFlightBookings.get(i); // for adding new entry in
																				// WAITING

					FlightBooking bookingInWaiting = FlightBooking.builder().flight(flight)
							.flightClass(existingBooking.getFlightClass()).passenger(passenger).bookingId(bookingId)
							.bookingTime(bookingTime).status(FlightBookingStatus.WAITING.value()).build();
					this.flightBookingService.add(bookingInWaiting);
				}

			}

			// now here we can clearly say that total seat which we require is NOT FULLY
			// AVAILABLE
			// some seat are available and some will have to be in WAITING
			else {

				int totalSeatsToAddInWaiting = request.getTotalPassengers() - totalAvailableSeat;

				// firstly confirm all the available seats
				for (FlightBooking booking : totalAvailableBookings) {
					booking.setBookingId(bookingId);
					booking.setBookingTime(bookingTime);
					booking.setStatus(FlightBookingStatus.CONFIRMED.value());
					booking.setPassenger(passenger);
					this.flightBookingService.add(booking);

					if (request.getFlightClassType().equals(FlightClassType.ECONOMY.value())) {
						passenger.setWalletAmount(passenger.getWalletAmount().subtract(flight.getEconomySeatFare()));

					} else if (request.getFlightClassType().equals(FlightClassType.BUSINESS.value())) {
						passenger.setWalletAmount(passenger.getWalletAmount().subtract(flight.getBusinessSeatFare()));

					} else if (request.getFlightClassType().equals(FlightClassType.FIRST_CLASS.value())) {
						passenger.setWalletAmount(passenger.getWalletAmount().subtract(flight.getFirstClassSeatFare()));

					}

					this.userService.registerUser(passenger);

				}

				// secondly add pending seats as waiting
				for (int i = 0; i < totalSeatsToAddInWaiting; i++) {
					FlightBooking existingBooking = totalFlightBookings.get(i); // for adding new entry in
					// WAITING

					FlightBooking bookingInWaiting = FlightBooking.builder().flight(flight)
							.flightClass(existingBooking.getFlightClass()).passenger(passenger).bookingId(bookingId)
							.bookingTime(bookingTime).status(FlightBookingStatus.WAITING.value()).build();

					this.flightBookingService.add(bookingInWaiting);
				}

			}

		}

		response.setResponseMessage(
				"Your Booking ID is [ " + bookingId + " ] , Please check the booking status in dashboard");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);

			LOG.info(jsonString);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<FlightBookingResponseDto> fetchAllFlightBookings() {

		LOG.info("Received request for fetching all bookings");

		FlightBookingResponseDto response = new FlightBookingResponseDto();

		List<FlightBooking> allBookings = new ArrayList<>();

		allBookings = this.flightBookingService.getByStatusIn(Arrays.asList(FlightBookingStatus.CONFIRMED.value(),
				FlightBookingStatus.CANCELLED.value(), FlightBookingStatus.WAITING.value()));

		if (CollectionUtils.isEmpty(allBookings)) {
			response.setResponseMessage("Failed to book the seats");
			response.setSuccess(true);

			return new ResponseEntity<FlightBookingResponseDto>(response, HttpStatus.OK);
		}

		response.setBookings(allBookings);
		response.setResponseMessage("Failed to book the seats");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);

			LOG.info(jsonString);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<FlightBookingResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<FlightBookingResponseDto> fetchBookingsByFlight(int flightId) {

		LOG.info("Received request for fetching all bookings by using flight id");

		FlightBookingResponseDto response = new FlightBookingResponseDto();

		if (flightId == 0) {
			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<FlightBookingResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<FlightBooking> allBookings = new ArrayList<>();

		Flight flight = this.flightService.getById(flightId);

		// all bookings with Available & Confirmed
		allBookings = this.flightBookingService.getByFlightAndStatusNotIn(flight,
				Arrays.asList(FlightBookingStatus.CANCELLED.value(), FlightBookingStatus.WAITING.value(),
						FlightBookingStatus.PENDING.value()));

		if (CollectionUtils.isEmpty(allBookings)) {
			response.setResponseMessage("Failed to book the seats");
			response.setSuccess(true);

			return new ResponseEntity<FlightBookingResponseDto>(response, HttpStatus.OK);
		}

		response.setBookings(allBookings);
		response.setResponseMessage("Failed to book the seats");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);

			LOG.info(jsonString);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<FlightBookingResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<FlightBookingResponseDto> fetchUserBookings(int userId) {

		LOG.info("Received request for fetching user bookings");

		FlightBookingResponseDto response = new FlightBookingResponseDto();

		if (userId == 0) {
			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<FlightBookingResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User passenger = this.userService.getUserById(userId);

		List<FlightBooking> allBookings = new ArrayList<>();

		allBookings = this.flightBookingService.getByPassenger(passenger);

		if (CollectionUtils.isEmpty(allBookings)) {
			response.setResponseMessage("Failed to book the seats");
			response.setSuccess(true);

			return new ResponseEntity<FlightBookingResponseDto>(response, HttpStatus.OK);
		}

		response.setBookings(allBookings);
		response.setResponseMessage("Failed to book the seats");
		response.setSuccess(true);

		return new ResponseEntity<FlightBookingResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> cancelFlightBooking(int bookingId) {

		CommonApiResponse response = new CommonApiResponse();

		if (bookingId == 0) {
			response.setResponseMessage("Failed to Cancel Booking, missing booking id");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		FlightBooking booking = this.flightBookingService.getById(bookingId);

		if (booking == null) {
			response.setResponseMessage("No Booking found, failed to cancel booking");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (booking.getStatus().equals(FlightBookingStatus.CANCELLED.value())) {
			response.setResponseMessage("Ticket is already cancelled");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (booking.getStatus().equals(FlightBookingStatus.WAITING.value())) {

			booking.setStatus(FlightBookingStatus.CANCELLED.value());
			flightBookingService.add(booking);

			response.setResponseMessage("Ticket Cancelled Successfully");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}

		if (booking.getStatus().equals(FlightBookingStatus.CONFIRMED.value())) {

			booking.setStatus(FlightBookingStatus.CANCELLED.value());
			flightBookingService.add(booking);

			// if waiting seat already present, then try to make that confirmed
			List<FlightBooking> waitingBookings = this.flightBookingService.getByFlightAndStatusNotInAndFlightClass(
					booking.getFlight(),
					Arrays.asList(FlightBookingStatus.AVAILABLE.value(), FlightBookingStatus.CANCELLED.value(),
							FlightBookingStatus.CONFIRMED.value(), FlightBookingStatus.PENDING.value()),
					booking.getFlightClass());

			if (!CollectionUtils.isEmpty(waitingBookings)) {

				boolean addNewAvailableSeat = true;

				for (FlightBooking waitingBooking : waitingBookings) {

					User user = waitingBooking.getPassenger();

					if (booking.getFlightClass().equals(FlightClassType.ECONOMY.value())) {
						// amount present in wallet
						if (user.getWalletAmount().compareTo(booking.getFlight().getEconomySeatFare()) > 0) {
							waitingBooking.setAirplaneSeatNo(booking.getAirplaneSeatNo());
							waitingBooking.setStatus(FlightBookingStatus.CONFIRMED.value());
							this.flightBookingService.add(booking);

							addNewAvailableSeat = false;

							break;
						}

					} else if (booking.getFlightClass().equals(FlightClassType.BUSINESS.value())) {

						// amount present in wallet
						if (user.getWalletAmount().compareTo(booking.getFlight().getBusinessSeatFare()) > 0) {
							waitingBooking.setAirplaneSeatNo(booking.getAirplaneSeatNo());
							waitingBooking.setStatus(FlightBookingStatus.CONFIRMED.value());
							this.flightBookingService.add(booking);

							addNewAvailableSeat = false;

							break;
						}

					} else if (booking.getFlightClass().equals(FlightClassType.FIRST_CLASS.value())) {

						// amount present in wallet
						if (user.getWalletAmount().compareTo(booking.getFlight().getFirstClassSeatFare()) > 0) {
							waitingBooking.setAirplaneSeatNo(booking.getAirplaneSeatNo());

							waitingBooking.setStatus(FlightBookingStatus.CONFIRMED.value());
							this.flightBookingService.add(booking);

							addNewAvailableSeat = false;

							break;
						}
					}

				}

				if (addNewAvailableSeat) {
					// now making new ticket available to customer with the same seat
					FlightBooking ticketToMakeAvailable = FlightBooking.builder().flight(booking.getFlight())
							.status(FlightBookingStatus.AVAILABLE.value()).airplaneSeatNo(booking.getAirplaneSeatNo())
							.flightClass(booking.getFlightClass()).build();

					flightBookingService.add(ticketToMakeAvailable);

				}

			}

			// if waiting seat is not there, then add new Available seat with same seat
			else {

				// now making new ticket available to customer with the same seat
				FlightBooking ticketToMakeAvailable = FlightBooking.builder().flight(booking.getFlight())
						.status(FlightBookingStatus.AVAILABLE.value()).airplaneSeatNo(booking.getAirplaneSeatNo())
						.flightClass(booking.getFlightClass()).build();

				flightBookingService.add(ticketToMakeAvailable);

			}

			response.setResponseMessage("Ticket Cancelled Successfully");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
		return null;

	}

	public void downloadBookingTicket(String bookingId, HttpServletResponse response)
			throws DocumentException, IOException {

		if (bookingId == null) {
			return;
		}

		List<FlightBooking> bookings = this.flightBookingService.getByBookingId(bookingId);

		if (bookings == null) {
			return;
		}

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + bookings.get(0).getPassenger().getName() + " "
				+ bookings.get(0).getBookingId() + "_flight_ticket.pdf";
		response.setHeader(headerKey, headerValue);

		TicketDownloader exporter = new TicketDownloader(bookings);
		exporter.export(response);

		return;

	}

	public ResponseEntity<FlightSeatDetailsResponse> fetchFlightSeatDetails(int flightId) {

		FlightSeatDetailsResponse response = new FlightSeatDetailsResponse();

		if (flightId == 0) {
			response.setResponseMessage("missing data");
			response.setSuccess(true);

			return new ResponseEntity<FlightSeatDetailsResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Flight flight = this.flightService.getById(flightId);

		List<FlightBooking> bookings = new ArrayList<>();

		bookings = this.flightBookingService.getByFlight(flight);

		if (CollectionUtils.isEmpty(bookings)) {

			response.setResponseMessage("bookings not found");
			response.setSuccess(true);

			return new ResponseEntity<FlightSeatDetailsResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		List<FlightBooking> economyAvailable = new ArrayList<>();
		List<FlightBooking> economyWaiting = new ArrayList<>();

		List<FlightBooking> businessAvailable = new ArrayList<>();
		List<FlightBooking> businessWaiting = new ArrayList<>();

		List<FlightBooking> firstClassAvailable = new ArrayList<>();
		List<FlightBooking> firstClassWaiting = new ArrayList<>();

		for (FlightBooking booking : bookings) {

			if (booking.getFlightClass().equals(FlightClassType.ECONOMY.value())
					&& booking.getStatus().equals(FlightBookingStatus.AVAILABLE.value())) {
				economyAvailable.add(booking);
			}

			else if (booking.getFlightClass().equals(FlightClassType.ECONOMY.value())
					&& booking.getStatus().equals(FlightBookingStatus.WAITING.value())) {
				economyWaiting.add(booking);
			}

			if (booking.getFlightClass().equals(FlightClassType.BUSINESS.value())
					&& booking.getStatus().equals(FlightBookingStatus.AVAILABLE.value())) {
				businessAvailable.add(booking);
			}

			else if (booking.getFlightClass().equals(FlightClassType.BUSINESS.value())
					&& booking.getStatus().equals(FlightBookingStatus.WAITING.value())) {
				businessWaiting.add(booking);
			}

			if (booking.getFlightClass().equals(FlightClassType.FIRST_CLASS.value())
					&& booking.getStatus().equals(FlightBookingStatus.AVAILABLE.value())) {
				firstClassAvailable.add(booking);
			}

			else if (booking.getFlightClass().equals(FlightClassType.FIRST_CLASS.value())
					&& booking.getStatus().equals(FlightBookingStatus.WAITING.value())) {
				firstClassWaiting.add(booking);
			}

		}

		response.setBusinessSeats(flight.getAirplane().getBusinessSeats());
		response.setEconomySeats(flight.getAirplane().getEconomySeats());
		response.setFirstClassSeats(flight.getAirplane().getFirstClassSeats());

		response.setFirstClassSeatsAvailable(firstClassAvailable.size());
		response.setBusinessSeatsAvailable(businessAvailable.size());
		response.setEconomySeatsAvailable(economyAvailable.size());

		response.setEconomySeatsWaiting(economyWaiting.size());
		response.setBusinessSeatsWaiting(businessWaiting.size());
		response.setFirstClassSeatsWaiting(firstClassWaiting.size());

		response.setTotalSeat(flight.getAirplane().getTotalSeat());

		response.setResponseMessage("flight seats fetch successful");
		response.setSuccess(true);

		return new ResponseEntity<FlightSeatDetailsResponse>(response, HttpStatus.OK);
	}

}
