import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";

const ViewAllAirplane = () => {
  const [allAirplanes, setAllAirplanes] = useState([]);

  const admin_token = sessionStorage.getItem("admin-jwtToken");

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

  useEffect(() => {
    const getAllAirplanes = async () => {
      const allAirplanes = await retrieveAllAirplanes();
      if (allAirplanes) {
        setAllAirplanes(allAirplanes.airplanes);
      }
    };

    getAllAirplanes();
  }, []);

  return (
    <div>
      <div className="mt-2">
        <div
          className="card form-card ms-5 me-5 mb-5 custom-bg border-color "
          style={{
            height: "30rem",
          }}
        >
          <div className="card-header custom-bg-text text-center bg-color">
            <h2>All Airplanes</h2>
          </div>
          <div
            className="card-body"
            style={{
              overflowY: "auto",
            }}
          >
            <div className="table-responsive">
              <table className="table table-hover text-color text-center">
                <thead className="table-bordered border-color bg-color custom-bg-text">
                  <tr>
                    <th scope="col">Airplane</th>
                    <th scope="col">Registration Number</th>
                    <th scope="col">Airplane Description</th>
                    <th scope="col">Total Seat</th>
                    <th scope="col">Total Economy Seat</th>
                    <th scope="col">Total Business Seat</th>
                    <th scope="col">Total First Class Seat</th>
                  </tr>
                </thead>
                <tbody>
                  {allAirplanes.map((airplane) => {
                    return (
                      <tr>
                        <td>
                          <b>{airplane.name}</b>
                        </td>

                        <td>
                          <b>{airplane.registrationNumber}</b>
                        </td>
                        <td>
                          <b>{airplane.description}</b>
                        </td>
                        <td>
                          <b>{airplane.totalSeat}</b>
                        </td>
                        <td>
                          <b>{airplane.economySeats}</b>
                        </td>
                        <td>
                          <b>{airplane.businessSeats}</b>
                        </td>
                        <td>
                          <b>{airplane.firstClassSeats}</b>
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
    </div>
  );
};

export default ViewAllAirplane;
