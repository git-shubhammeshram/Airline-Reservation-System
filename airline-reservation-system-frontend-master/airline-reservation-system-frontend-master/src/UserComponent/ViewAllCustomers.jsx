import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ViewAllCustomers = () => {
  let navigate = useNavigate();
  const [allCustomer, setAllCustomer] = useState([]);

  const retrieveAllCustomers = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/user/fetch/role?role=PASSENGER"
    );
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getAllCustomers = async () => {
      const customers = await retrieveAllCustomers();
      if (customers) {
        setAllCustomer(customers.users);
      }
    };

    getAllCustomers();
  }, []);

  const viewAccountDetails = (customerId) => {};

  const activateUser = (customerId) => {};

  const deactivateUser = (customerId) => {};

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
            <h2>All Bank Customers</h2>
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
                    <th scope="col">Customer Name</th>
                    <th scope="col">Bank Name</th>
                    <th scope="col">Email</th>
                    <th scope="col">Gender</th>
                    <th scope="col">Contact</th>
                    <th scope="col">Street</th>
                    <th scope="col">City</th>
                    <th scope="col">Pincode</th>
                    <th scope="col">Account Details</th>
                    <th scope="col">Status</th>
                    <th scope="col">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {allCustomer.map((customer) => {
                    return (
                      <tr>
                        <td>
                          <b>{customer.name}</b>
                        </td>
                        <td>
                          <b>{customer.bank.name}</b>
                        </td>
                        <td>
                          <b>{customer.email}</b>
                        </td>
                        <td>
                          <b>{customer.gender}</b>
                        </td>
                        <td>
                          <b>{customer.contact}</b>
                        </td>
                        <td>
                          <b>{customer.street}</b>
                        </td>
                        <td>
                          <b>{customer.city}</b>
                        </td>
                        <td>
                          <b>{customer.pincode}</b>
                        </td>
                        <td>
                          <button
                            onClick={() => viewAccountDetails(customer.id)}
                            className="btn btn-sm bg-color custom-bg-text"
                          >
                            View Account
                          </button>
                          <ToastContainer />
                        </td>
                        <td>
                          <b>{customer.status}</b>
                        </td>
                        <td>
                          {(() => {
                            if (customer.status === "Active") {
                              return (
                                <button
                                  onClick={() => deactivateUser(customer.id)}
                                  className="btn btn-sm bg-color custom-bg-text ms-2"
                                >
                                  Deactivate
                                </button>
                              );
                            }
                          })()}

                          {(() => {
                            if (customer.status !== "Active") {
                              return (
                                <button
                                  onClick={() => activateUser(customer.id)}
                                  className="btn btn-sm bg-color custom-bg-text ms-2"
                                >
                                  Activate
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
    </div>
  );
};

export default ViewAllCustomers;
