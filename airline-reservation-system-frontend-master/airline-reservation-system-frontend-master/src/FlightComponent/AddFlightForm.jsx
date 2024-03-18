import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const AddFlightForm = () => {
  const navigate = useNavigate();

  const admin_token = sessionStorage.getItem("admin-jwtToken");

  const [flight, setFlight] = useState({
    departureTime: "",
    arrivalTime: "",
    departureAirportId: "",
    arrivalAirportId: "",
    airplaneId: "",
    status: "",
    economySeatFare: "",
    businessSeatFare: "",
    firstClassSeatFare: "",
  });

  const handleUserInput = (e) => {
    setFlight({ ...flight, [e.target.name]: e.target.value });
  };

  const [departureTime, setDepartureTime] = useState("");
  const [arrivalTime, setArrivalTime] = useState("");

  const [allAirplanes, setAllAirplanes] = useState([]);
  const [allAirports, setAllAirports] = useState([]);
  const [allStatus, setAllStatus] = useState([]);

  const retrieveAllAirplanes = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/airplane/fetch/all",
      {
        headers: {
          Authorization: "Bearer " + admin_token, // Replace with your actual JWT token
        },
      }
    );
    console.log(response.data);
    return response.data;
  };

  const retrieveAllAirport = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/airport/fetch/all"
    );
    console.log(response.data);
    return response.data;
  };

  const retrieveAllFlightStatus = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/flight/status/all",
      {
        headers: {
          Authorization: "Bearer " + admin_token, // Replace with your actual JWT token
        },
      }
    );
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getAllAirplanes = async () => {
      const allAirplanes = await retrieveAllAirplanes();
      if (allAirplanes) {
        setAllAirplanes(allAirplanes.airplanes);
      }
    };

    const getAllAirports = async () => {
      const allAirports = await retrieveAllAirport();
      if (allAirports) {
        setAllAirports(allAirports.airports);
      }
    };

    const getAllFlightStatus = async () => {
      const allStatuses = await retrieveAllFlightStatus();
      if (allStatuses) {
        setAllStatus(allStatuses);
      }
    };

    getAllAirplanes();
    getAllAirports();
    getAllFlightStatus();
  }, []);

  const saveFlight = (e) => {
    const departureEpochTime = new Date(departureTime).getTime();
    flight.departureTime = departureEpochTime;

    const arrivalEpochTime = new Date(arrivalTime).getTime();
    flight.arrivalTime = arrivalEpochTime;

    fetch("http://localhost:8080/api/flight/add", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + admin_token,
      },
      body: JSON.stringify(flight),
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
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
        <div
          className="card form-card border-color text-color custom-bg"
          style={{ width: "50rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h5 className="card-title">Add Flight</h5>
          </div>
          <div className="card-body">
            <form className="row g-3" onSubmit={saveFlight}>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="trainId" className="form-label">
                  <b>Airplane</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="airplaneId"
                >
                  <option value="">Select Airplane</option>

                  {allAirplanes.map((airplane) => {
                    return (
                      <option value={airplane.id}> {airplane.name} </option>
                    );
                  })}
                </select>
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="trainId" className="form-label">
                  <b>Departure Airport</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="departureAirportId"
                >
                  <option value="">Select Departure Airport</option>

                  {allAirports.map((airport) => {
                    return <option value={airport.id}> {airport.name} </option>;
                  })}
                </select>
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="trainId" className="form-label">
                  <b>Arrival Airport</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="arrivalAirportId"
                >
                  <option value="">Select Arrival Airport</option>

                  {allAirports.map((airport) => {
                    return <option value={airport.id}> {airport.name} </option>;
                  })}
                </select>
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="trainId" className="form-label">
                  <b>Flight Status</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="status"
                >
                  <option value="">Select Status</option>

                  {allStatus.map((status) => {
                    return <option value={status}> {status} </option>;
                  })}
                </select>
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Select Departure Time</b>
                </label>
                <input
                  type="datetime-local"
                  className="form-control"
                  value={departureTime}
                  onChange={(e) => setDepartureTime(e.target.value)}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Select Departure Time</b>
                </label>
                <input
                  type="datetime-local"
                  className="form-control"
                  value={arrivalTime}
                  onChange={(e) => setArrivalTime(e.target.value)}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <b>
                  <label className="form-label">Economy Seat Fare</label>
                </b>
                <input
                  type="number"
                  className="form-control"
                  id="economySeatFare"
                  name="economySeatFare"
                  onChange={handleUserInput}
                  value={flight.economySeatFare}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <b>
                  <label className="form-label">Business Seat Fare</label>
                </b>
                <input
                  type="number"
                  className="form-control"
                  id="businessSeatFare"
                  name="businessSeatFare"
                  onChange={handleUserInput}
                  value={flight.businessSeatFare}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <b>
                  <label className="form-label">First Class Seat Fare</label>
                </b>
                <input
                  type="number"
                  className="form-control"
                  id="firstClassSeatFare"
                  name="firstClassSeatFare"
                  onChange={handleUserInput}
                  value={flight.firstClassSeatFare}
                />
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Add Flight"
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

export default AddFlightForm;
