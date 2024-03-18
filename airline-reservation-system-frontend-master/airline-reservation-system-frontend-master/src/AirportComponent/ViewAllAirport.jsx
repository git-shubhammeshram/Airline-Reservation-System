import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";

const ViewAllAirport = () => {
  const [allAirports, setAllAirports] = useState([]);

  const retrieveAllAirport = async () => {
    const response = await axios.get("http://localhost:8080/api/airport/fetch/all");
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getAllAirports = async () => {
      const allAirports = await retrieveAllAirport();
      if (allAirports) {
        setAllAirports(allAirports.airports);
      }
    };

    getAllAirports();
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
            <h2>All Airports</h2>
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
                    <th scope="col">Airport</th>
                    <th scope="col">Airport Location</th>
                    <th scope="col">Airport Code</th>
                    <th scope="col">Airport Address</th>
                  </tr>
                </thead>
                <tbody>
                  {allAirports.map((airport) => {
                    return (
                      <tr>
                        <td>
                          <b>{airport.name}</b>
                        </td>
                        <td>
                          <b>{airport.location}</b>
                        </td>
                        <td>
                          <b>{airport.code}</b>
                        </td>
                        <td>
                          <b>{airport.address}</b>
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

export default ViewAllAirport;
