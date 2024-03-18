import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useLocation } from "react-router-dom";

const UpdateFlightStatusForm = () => {
  const navigate = useNavigate();

  const admin_token = sessionStorage.getItem("admin-jwtToken");

  const location = useLocation();
  const flight = location.state;

  const [allStatus, setAllStatus] = useState([]);

  const [updateFlightStatus, setUpdateFlightStatus] = useState({
    flightId: flight.id,
    status: "",
  });

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    const formattedDate = date.toLocaleString(); // Adjust the format as needed

    return formattedDate;
  };

  const handleUserInput = (e) => {
    setUpdateFlightStatus({
      ...updateFlightStatus,
      [e.target.name]: e.target.value,
    });
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
    const getAllFlightStatus = async () => {
      const allStatuses = await retrieveAllFlightStatus();
      if (allStatuses) {
        setAllStatus(allStatuses);
      }
    };

    getAllFlightStatus();
  }, []);

  const updateFlight = (e) => {
    fetch("http://localhost:8080/api/flight/update/status", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + admin_token,
      },
      body: JSON.stringify(updateFlightStatus),
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
            <h5 className="card-title">Update Flight</h5>
          </div>
          <div className="card-body">
            <form className="row g-3" onSubmit={updateFlight}>
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

              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Update Flight Status"
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

export default UpdateFlightStatusForm;
