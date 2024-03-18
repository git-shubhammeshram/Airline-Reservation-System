import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";

const ViewScheduledFlightBookings = () => {
  const navigate = useNavigate();

  const location = useLocation();
  const flight = location.state;

  const [scheduledFlightBookings, setScheduledFlightBookings] = useState([]);

  const [flightSeatDetail, setFlightSeatDetail] = useState({});

  const retrieveScheduleFlightBookings = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/flight/book/fetch?flightId=" + flight.id
    );
    console.log(response.data);
    return response.data;
  };

  const retrieveFlightSeatDetails = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/flight/book/fetch/seatDetails?flightId=" +
        flight.id
    );
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getFlightBookings = async () => {
      const flightTickets = await retrieveScheduleFlightBookings();
      if (flightTickets) {
        setScheduledFlightBookings(flightTickets.bookings);
      }
    };

    const getFlightSeatDetails = async () => {
      const flightSeatDetails = await retrieveFlightSeatDetails();
      if (flightSeatDetails) {
        setFlightSeatDetail(flightSeatDetails);
      }
    };

    getFlightBookings();
    getFlightSeatDetails();
  }, []);

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    const formattedDate = date.toLocaleString(); // Adjust the format as needed

    return formattedDate;
  };

  const bookFlightTicket = () => {
    navigate("/passenger/flight/book", { state: flight });
  };

  return (
    <div className="mt-3">
      <div className="d-flex justify-content-center align-items-center">
        <div
          className="card form-card ms-2 me-2 mb-5 custom-bg border-color "
          style={{
            height: "45rem",
            width: "40rem",
          }}
        >
          <div className="card-header custom-bg-text text-center bg-color">
            <h2>Check Flight Ticket Availablity</h2>
          </div>
          <div className="card-body" style={{ overflowY: "auto" }}>
            <div className="row">
              <div className="col-md-6">
                <div className="mt-3">
                  <b>Flight Number:</b>
                  <h5 className="text-color"> {flight.flightNumber}</h5>
                </div>
                <div className="mt-3">
                  <b>Airplane:</b>
                  <h5 className="text-color"> {flight.airplane.name}</h5>
                </div>
                <div className="mt-3">
                  <b>Departure Airport:</b>
                  <h5 className="text-color">{flight.departureAirport.name}</h5>
                </div>
                <div className="mt-3">
                  <b>Arrival Airport:</b>
                  <h5 className="text-color"> {flight.arrivalAirport.name}</h5>
                </div>
                <div className="mt-3">
                  <b>Departure Timing:</b>
                  <h5 className="text-color">
                    {formatDateFromEpoch(flight.departureTime)}
                  </h5>
                </div>
                <div className="mt-3">
                  <b>Arrival Timing:</b>
                  <h5 className="text-color">
                    {formatDateFromEpoch(flight.arrivalTime)}
                  </h5>
                </div>

                <div className="mt-3">
                  <b>Economy Seat Price (in Rs):</b>
                  <h5 className="text-color"> {flight.economySeatFare}</h5>
                </div>
                <div className="mt-3">
                  <b>Business Seat Price (in Rs):</b>
                  <h5 className="text-color"> {flight.businessSeatFare}</h5>
                </div>
                <div className="mt-3">
                  <b>First Class Seat Price (in Rs):</b>
                  <h5 className="text-color"> {flight.firstClassSeatFare}</h5>
                </div>

                <hr className="my-4" />
                <div className="mt-3">
                  <b>Total Seat:</b>
                  <h5 className="text-color">{flight.totalSeat}</h5>
                </div>
                <div className="mt-3">
                  <b>Total Economy Seat:</b>
                  <h5 className="text-color">
                    {flightSeatDetail.economySeats}
                  </h5>
                </div>
                <div className="mt-3">
                  <b>Total Economy Seat Available:</b>
                  <h5 className="text-color">
                    {flightSeatDetail.economySeatsAvailable}
                  </h5>
                </div>
                <div className="mt-3">
                  <b>Total Business Seat:</b>
                  <h5 className="text-color">
                    {flightSeatDetail.businessSeats}
                  </h5>
                </div>
                <div className="mt-3">
                  <b>Total Business Seat Available:</b>
                  <h5 className="text-color">
                    {flightSeatDetail.businessSeatsAvailable}
                  </h5>
                </div>
                <div className="mt-3">
                  <b>Total First Class Seat:</b>
                  <h5 className="text-color">
                    {flightSeatDetail.firstClassSeats}
                  </h5>
                </div>
                <div className="mt-3">
                  <b>Total First Class Seat Available:</b>
                  <h5 className="text-color">
                    {flightSeatDetail.firstClassSeatsAvailable}
                  </h5>
                </div>
              </div>
              <div className="col-md-5">
                <div className="table-responsive" style={{ float: "right" }}>
                  <div className="text-center">
                    <h4 className="text-color">Flight Seats</h4>
                  </div>
                  <table className="table table-hover text-color text-center">
                    <thead className="table-bordered border-color bg-color custom-bg-text">
                      <tr>
                        <th scope="col">Flight Seat</th>
                        <th scope="col">Booking Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {scheduledFlightBookings.map((booking) => {
                        return (
                          <tr>
                            <td>
                              <b>{booking.airplaneSeatNo.seatNo}</b>
                            </td>
                            <td>
                              <b>{booking.status}</b>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

            <div className="mt-3 d-flex justify-content-center align-items-center">
              <button
                onClick={() => bookFlightTicket()}
                className="btn btn-lg bg-color custom-bg-text"
              >
                Book Ticket
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewScheduledFlightBookings;
