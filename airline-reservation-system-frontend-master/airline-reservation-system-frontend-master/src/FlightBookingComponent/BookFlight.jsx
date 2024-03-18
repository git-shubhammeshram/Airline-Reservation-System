import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useLocation } from "react-router-dom";

const BookFlight = () => {
  var passenger = JSON.parse(sessionStorage.getItem("active-passenger"));
  var passengerToken = sessionStorage.getItem("passenger-jwtToken");

  const navigate = useNavigate();

  const location = useLocation();
  const flight = location.state;
  const [allFlightClass, setAllFlightClass] = useState([]);

  const [bookFlight, setBookFlight] = useState({
    flightId: flight.id,
    flightClassType: "",
    passengerId: "",
    totalPassengers: "",
  });

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    const formattedDate = date.toLocaleString(); // Adjust the format as needed

    return formattedDate;
  };

  const handleUserInput = (e) => {
    setBookFlight({
      ...bookFlight,
      [e.target.name]: e.target.value,
    });
  };

  const retrieveAllFlightClass = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/flight/class/all"
    );
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getAllFlightClass = async () => {
      const allFlightClass = await retrieveAllFlightClass();
      if (allFlightClass) {
        setAllFlightClass(allFlightClass);
      }
    };

    getAllFlightClass();
  }, []);

  const bookPassendgerFlight = (e) => {
    if (passengerToken !== null) {
      bookFlight.passengerId = passenger.id;

      fetch("http://localhost:8080/api/flight/book/add", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + passengerToken,
        },
        body: JSON.stringify(bookFlight),
      })
        .then((result) => {
          console.log("result", result);
          result.json().then((res) => {
            console.log(res);

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
                navigate("/admin/flight/all");
              }, 1000); // Redirect after 3 seconds
            } else {
              console.log("Didn't got success response");
              toast.error("It seems server is down", {
                position: "top-center",
                autoClose: 1000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
              });
              // setTimeout(() => {
              //   window.location.reload(true);
              // }, 1000); // Redirect after 3 seconds
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
          // setTimeout(() => {
          //   window.location.reload(true);
          // }, 1000); // Redirect after 3 seconds
        });
      e.preventDefault();
    } else {
      toast.warn("Please Login to Book the Seat..!!!", {
        position: "top-center",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      e.preventDefault();
    }
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
        <div
          className="card form-card border-color text-color custom-bg"
          style={{ width: "50rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h5 className="card-title">Book Flight</h5>
          </div>
          <div className="card-body">
            <form className="row g-3" onSubmit={bookPassendgerFlight}>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Airplane</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={flight.airplane.name}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Departure Airport</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={flight.departureAirport.name}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Arrival Airport</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={flight.arrivalAirport.name}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Flight Status</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={flight.status}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Departure Time</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={formatDateFromEpoch(flight.departureTime)}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Arrival Time</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={formatDateFromEpoch(flight.arrivalTime)}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Economy Seat Fare</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={flight.economySeatFare}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Business Seat Fare</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={flight.businessSeatFare}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>First Class Seat Fare</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={flight.firstClassSeatFare}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <b>
                  <label className="form-label">Total Passenger</label>
                </b>
                <input
                  type="number"
                  className="form-control"
                  id="totalPassengers"
                  name="totalPassengers"
                  onChange={handleUserInput}
                  value={flight.totalPassengers}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="trainId" className="form-label">
                  <b>Flight Class type</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="flightClassType"
                >
                  <option value="">Select Flight Class</option>

                  {allFlightClass.map((flightClass) => {
                    return <option value={flightClass}> {flightClass} </option>;
                  })}
                </select>
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Book Seat"
                />
              </div>
              <ToastContainer />
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookFlight;
