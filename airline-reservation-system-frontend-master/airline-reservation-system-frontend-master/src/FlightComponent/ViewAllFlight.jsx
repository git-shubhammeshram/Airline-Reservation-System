import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";

const ViewAllFlight = () => {
  const navigate = useNavigate();

  var passenger = JSON.parse(sessionStorage.getItem("active-passenger"));
  var admin = JSON.parse(sessionStorage.getItem("active-admin"));

  const [searchRequest, setSearchRequest] = useState({
    startTime: "",
    endTime: "",
    fromAirportId: "",
    toAirportId: "",
  });

  const [tempSearchRequest, setTempSearchRequest] = useState({
    startTime: "",
    endTime: "",
    fromAirportId: "",
    toAirportId: "",
  });

  const handleTempSearchInput = (e) => {
    setTempSearchRequest({
      ...tempSearchRequest,
      [e.target.name]: e.target.value,
    });
  };

  const [airports, setAirports] = useState([]);

  const retrieveAllAirports = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/airport/fetch/all"
    );
    return response.data;
  };

  useEffect(() => {
    const getAllAirports = async () => {
      const allAirports = await retrieveAllAirports();
      if (allAirports) {
        setAirports(allAirports.airports);
      }
    };

    getAllAirports();
  }, []);

  const [scheduledFlights, setScheduledFlights] = useState([]);

  const retrieveAllScheduledFlights = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/flight/fetch/all"
    );
    console.log(response.data);
    return response.data;
  };

  const searchScheduledFlightsByUserInput = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/flight/search?startTime=" +
        convertToEpochTime(searchRequest.startTime) +
        "&endTime=" +
        convertToEpochTime(searchRequest.endTime) +
        "&fromAirportId=" +
        searchRequest.fromAirportId +
        "&endAirportId=" +
        searchRequest.toAirportId
    );
    console.log(response.data);
    return response.data;
  };

  const convertToEpochTime = (dateString) => {
    const selectedDate = new Date(dateString);
    const epochTime = selectedDate.getTime();
    return epochTime;
  };

  useEffect(() => {
    const getAllScheduledFlights = async () => {
      if (
        searchRequest.startTime &&
        searchRequest.endTime &&
        searchRequest.fromAirportId &&
        searchRequest.toAirportId
      ) {
        const flights = await searchScheduledFlightsByUserInput();
        if (flights) {
          setScheduledFlights(flights.flights);
        }
      } else {
        const flights = await retrieveAllScheduledFlights();
        if (flights) {
          setScheduledFlights(flights.flights);
        }
      }
    };

    getAllScheduledFlights();
  }, [searchRequest]);

  const bookSeat = (flight) => {
    navigate("/passenger/flight/book", { state: flight });
  };

  const updateStatus = (flight) => {
    navigate("/admin/flight/status/update", { state: flight });
  };

  const navigateToViewFlightBookingStatus = (flight) => {
    navigate("/flight/booking/status/", { state: flight });
  };

  const searchScheduledTrains = (e) => {
    setSearchRequest(tempSearchRequest);
    e.preventDefault();
  };

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    const formattedDate = date.toLocaleString(); // Adjust the format as needed

    return formattedDate;
  };

  return (
    <div className="mt-3">
      <div
        className="card form-card ms-2 me-2 mb-5 custom-bg border-color "
        style={{
          height: "45rem",
        }}
      >
        <div className="card-header custom-bg-text text-center bg-color">
          <h2>Scheduled Flights</h2>
        </div>
        <div
          className="card-body"
          style={{
            overflowY: "auto",
          }}
        >
          <div className="row">
            <div className="col">
              <form class="row g-3 align-items-center">
                <div class="col-auto">
                  <label>
                    <b>Select Start Date</b>
                  </label>
                  <input
                    type="date"
                    class="form-control"
                    name="startTime"
                    placeholder="Start Time..."
                    onChange={handleTempSearchInput}
                    value={tempSearchRequest.startTime}
                    required
                  />
                </div>
                <div class="col-auto">
                  <label>
                    <b>Select End Date</b>
                  </label>
                  <input
                    type="date"
                    class="form-control"
                    name="endTime"
                    placeholder="Start Date..."
                    onChange={handleTempSearchInput}
                    value={tempSearchRequest.endTime}
                    required
                  />
                </div>

                <div className="col-auto">
                  <label>
                    <b>From Airport</b>
                  </label>
                  <select
                    onChange={handleTempSearchInput}
                    className="form-control"
                    name="fromAirportId"
                    required
                  >
                    <option value="">Select Source Airport</option>

                    {airports.map((airport) => {
                      return (
                        <option value={airport.id}> {airport.name} </option>
                      );
                    })}
                  </select>
                </div>

                <div className="col-auto">
                  <label>
                    <b>To Airport</b>
                  </label>
                  <select
                    onChange={handleTempSearchInput}
                    className="form-control"
                    name="toAirportId"
                    required
                  >
                    <option value="">Select Destination Airport</option>

                    {airports.map((airport) => {
                      return (
                        <option value={airport.id}> {airport.name} </option>
                      );
                    })}
                  </select>
                </div>

                <div class="col-auto">
                  <button
                    type="submit"
                    class="btn bg-color custom-bg-text btn-lg"
                    onClick={searchScheduledTrains}
                  >
                    Search
                  </button>
                </div>
              </form>
            </div>
          </div>
          <div className="table-responsive mt-3">
            <table className="table table-hover text-color text-center">
              <thead className="table-bordered border-color bg-color custom-bg-text">
                <tr>
                  <th scope="col">Flight Number</th>
                  <th scope="col">Airplane</th>
                  <th scope="col">Airplane Registration</th>
                  <th scope="col">Departure Time</th>
                  <th scope="col">Arrival Time</th>
                  <th scope="col">Source Airport</th>
                  <th scope="col">Destination Airport</th>
                  <th scope="col">Economy Fare (Rs.)</th>
                  <th scope="col">Business Fare (Rs.)</th>
                  <th scope="col">First Class Fare (Rs.)</th>
                  <th scope="col">Status</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {scheduledFlights.map((flight) => {
                  return (
                    <tr>
                      <td>
                        <b>{flight.flightNumber}</b>
                      </td>
                      <td>
                        <b>{flight.airplane.name}</b>
                      </td>
                      <td>
                        <b>{flight.airplane.registrationNumber}</b>
                      </td>
                      <td>
                        <b>{formatDateFromEpoch(flight.departureTime)}</b>
                      </td>
                      <td>
                        <b>{formatDateFromEpoch(flight.arrivalTime)}</b>
                      </td>
                      <td>
                        <b>{flight.departureAirport.name}</b>
                      </td>
                      <td>
                        <b>{flight.arrivalAirport.name}</b>
                      </td>
                      <td>
                        <b>{flight.economySeatFare}</b>
                      </td>
                      <td>
                        <b>{flight.businessSeatFare}</b>
                      </td>
                      <td>
                        <b>{flight.firstClassSeatFare}</b>
                      </td>
                      <td>
                        <b>{flight.status}</b>
                      </td>

                      <td>
                        {(() => {
                          if (admin !== null) {
                            return (
                              <div>
                                <button
                                  onClick={() => updateStatus(flight)}
                                  className="btn btn-sm bg-color custom-bg-text ms-2"
                                >
                                  Update Status
                                </button>

                                <button
                                  onClick={() =>
                                    navigateToViewFlightBookingStatus(flight)
                                  }
                                  className="btn btn-sm bg-color custom-bg-text"
                                >
                                  Book Seat
                                </button>
                              </div>
                            );
                          } else {
                            return (
                              <button
                                onClick={() =>
                                  navigateToViewFlightBookingStatus(flight)
                                }
                                className="btn btn-sm bg-color custom-bg-text"
                              >
                                Book Seat
                              </button>
                            );
                          }
                        })()}

                        <ToastContainer />
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewAllFlight;
