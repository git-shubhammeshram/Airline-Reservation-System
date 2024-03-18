import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";

const ViewPassengerFlightBooking = () => {
  const navigate = useNavigate();

  const passenger_token = sessionStorage.getItem("passenger-jwtToken");

  const [bookedFlights, setBookedFlights] = useState([]);

  var passenger = JSON.parse(sessionStorage.getItem("active-passenger"));

  const retrieveAllBookedFlights = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/flight/book/fetch/user?userId=" + passenger.id,
      {
        headers: {
          Authorization: "Bearer " + passenger_token, // Replace with your actual JWT token
        },
      }
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
    const getAllBookedFlights = async () => {
      const bookings = await retrieveAllBookedFlights();
      if (bookings) {
        setBookedFlights(bookings.bookings);
      }
    };

    getAllBookedFlights();
  }, []);

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    const formattedDate = date.toLocaleString(); // Adjust the format as needed

    return formattedDate;
  };

  const cancelFlightTicket = (bookingId) => {
    fetch(
      "http://localhost:8080/api/flight/book/ticket/cancel?bookingId=" +
        bookingId,
      {
        method: "DELETE",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/js on",
          Authorization: "Bearer " + passenger_token,
        },
      }
    )
      .then((result) => {
        result.json().then((res) => {
          if (res.success) {
            console.log("Got the success response");

            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });

            setTimeout(() => {
              window.location.reload(true);
            }, 3000); // Redirect after 3 seconds
          } else {
            console.log("Failed to delete the Train");
            toast.error("It seems server is down", {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          }
        });
      })
      .catch((error) => {
        console.error(error);
        toast.error("It seems server is down", {
          position: "top-center",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      });
  };

  const downloadTicket = (bookingId) => {
    fetch(
      `http://localhost:8080/api/flight/book/download/ticket?bookingId=${bookingId}`,
      {
        method: "GET",
        headers: {
          Authorization: "Bearer " + passenger_token,
        },
      }
    )
      .then((response) => response.blob())
      .then((blob) => {
        // Create a temporary URL for the blob
        const url = window.URL.createObjectURL(blob);

        // Create a temporary <a> element to trigger the download
        const link = document.createElement("a");
        link.href = url;
        link.download = "ticket.pdf"; // Specify the desired filename here

        // Append the link to the document and trigger the download
        document.body.appendChild(link);
        link.click();

        // Clean up the temporary URL and link
        URL.revokeObjectURL(url);
        document.body.removeChild(link);
      })
      .catch((error) => {
        console.error("Download error:", error);
      });
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
          <h2>Booked Flights</h2>
        </div>
        <div
          className="card-body"
          style={{
            overflowY: "auto",
          }}
        >
          <div className="table-responsive mt-3">
            <table className="table table-hover text-color text-center">
              <thead className="table-bordered border-color bg-color custom-bg-text">
                <tr>
                  <th scope="col">Booking Id</th>
                  <th scope="col">Flight Number</th>
                  <th scope="col">Airplane</th>
                  <th scope="col">Airplane Registration No.</th>
                  <th scope="col">Departure Time</th>
                  <th scope="col">Arrival Time</th>
                  <th scope="col">Source Airport</th>
                  <th scope="col">Destination Airport</th>
                  <th scope="col">Flight Class</th>
                  <th scope="col">Seat Fare (Rs.)</th>
                  <th scope="col">Seat No</th>
                  <th scope="col">Booking Time</th>
                  <th scope="col">Status</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {bookedFlights.map((book) => {
                  return (
                    <tr>
                      <td>
                        <b>{book.bookingId}</b>
                      </td>
                      <td>
                        <b>{book.flight.flightNumber}</b>
                      </td>
                      <td>
                        <b>{book.flight.airplane.name}</b>
                      </td>
                      <td>
                        <b>{book.flight.airplane.registrationNumber}</b>
                      </td>
                      <td>
                        <b>{formatDateFromEpoch(book.flight.departureTime)}</b>
                      </td>
                      <td>
                        <b>{formatDateFromEpoch(book.flight.arrivalTime)}</b>
                      </td>
                      <td>
                        <b>{book.flight.departureAirport.name}</b>
                      </td>
                      <td>
                        <b>{book.flight.arrivalAirport.name}</b>
                      </td>
                      <td>
                        <b>{book.flightClass}</b>
                      </td>
                      <td>
                        {(() => {
                          if (book.flightClass === "Economy") {
                            return <b>{book.flight.economySeatFare}</b>;
                          } else if (book.flightClass === "Business") {
                            return <b>{book.flight.businessSeatFare}</b>;
                          } else if (book.flightClass === "First Class") {
                            return <b>{book.flight.firstClassSeatFare}</b>;
                          }
                        })()}
                      </td>
                      <td>
                        {(() => {
                          if (book.airplaneSeatNo !== null) {
                            return <b> {book.airplaneSeatNo.seatNo}</b>;
                          }
                        })()}
                      </td>
                      <td>
                        <b>{formatDateFromEpoch(book.bookingTime)}</b>
                      </td>
                      <td>
                        <b>{book.status}</b>
                      </td>
                      <td>
                        {(() => {
                          if (book.status !== "Cancelled") {
                            return (
                              <button
                                onClick={() => cancelFlightTicket(book.id)}
                                className="btn btn-sm bg-color custom-bg-text"
                              >
                                Cancel Ticket
                              </button>
                            );
                          }
                        })()}

                        {(() => {
                          if (book.status === "Confirmed") {
                            return (
                              <button
                                onClick={() => downloadTicket(book.bookingId)}
                                className="btn btn-sm bg-color custom-bg-text ms-2"
                              >
                                Download Ticket
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

export default ViewPassengerFlightBooking;
