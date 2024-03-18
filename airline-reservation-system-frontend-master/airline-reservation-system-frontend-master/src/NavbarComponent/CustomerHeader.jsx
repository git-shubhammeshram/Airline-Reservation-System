import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

const CustomerHeader = () => {
  let navigate = useNavigate();

  const userLogout = () => {
    toast.success("logged out!!!", {
      position: "top-center",
      autoClose: 2000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    sessionStorage.removeItem("active-passenger");
    sessionStorage.removeItem("passenger-jwtToken");

    setTimeout(() => {
      navigate("/home");
    }, 1000); // Redirect after 3 seconds

    window.location.reload(true);
  };

  return (
    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 me-5">
      <li className="nav-item">
        <Link
          to="/passenger/wallet"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">My Wallet</b>
        </Link>
      </li>

      <li className="nav-item">
        <Link
          to="/passenger/flight/booking/all"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">View Flight Bookings</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to=""
          class="nav-link active"
          aria-current="page"
          onClick={userLogout}
        >
          <b className="text-color">Logout</b>
        </Link>
        <ToastContainer />
      </li>
    </ul>
  );
};

export default CustomerHeader;
