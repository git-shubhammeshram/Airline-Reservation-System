import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const AddAirplaneForm = () => {
  const navigate = useNavigate();

  const admin_token = sessionStorage.getItem("admin-jwtToken");

  const [airplane, setAirplane] = useState({
    name: "",
    registrationNumber: "",
    totalSeat: "",
    description: "",
    economySeats: "",
    businessSeats: "",
    firstClassSeats: "",
  });

  const handleUserInput = (e) => {
    setAirplane({ ...airplane, [e.target.name]: e.target.value });
  };

  const saveAirplane = (e) => {
    fetch("http://localhost:8080/api/airplane/add", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + admin_token,
      },
      body: JSON.stringify(airplane),
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
              navigate("/admin/airplane/all");
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
            <h5 className="card-title">Add Airplane</h5>
          </div>
          <div className="card-body">
            <form className="row g-3" onSubmit={saveAirplane}>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Airplane Name</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="name"
                  name="name"
                  onChange={handleUserInput}
                  value={airplane.name}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <b>
                  <label className="form-label">
                    Airplane Registration No.
                  </label>
                </b>
                <input
                  type="text"
                  className="form-control"
                  id="registrationNumber"
                  name="registrationNumber"
                  onChange={handleUserInput}
                  value={airplane.registrationNumber}
                />
              </div>

              <div class="mb-3 text-color">
                <label for="description" class="form-label">
                  <b>Airplane Description</b>
                </label>
                <textarea
                  class="form-control"
                  id="description"
                  rows="3"
                  name="description"
                  placeholder="enter description.."
                  onChange={handleUserInput}
                  value={airplane.description}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Total Seat</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="totalSeat"
                  name="totalSeat"
                  onChange={handleUserInput}
                  value={airplane.totalSeat}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Total Economy Seat</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="economySeats"
                  name="economySeats"
                  onChange={handleUserInput}
                  value={airplane.economySeats}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Total Business Seat</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="businessSeats"
                  name="businessSeats"
                  onChange={handleUserInput}
                  value={airplane.businessSeats}
                />
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Total First Class Seat</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="firstClassSeats"
                  name="firstClassSeats"
                  onChange={handleUserInput}
                  value={airplane.firstClassSeats}
                />
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Add Airplane"
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

export default AddAirplaneForm;
