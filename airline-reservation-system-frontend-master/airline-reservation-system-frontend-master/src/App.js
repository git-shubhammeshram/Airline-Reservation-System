import "./App.css";
import { Route, Routes } from "react-router-dom";
import AboutUs from "./page/AboutUs";
import ContactUs from "./page/ContactUs";
import Header from "./NavbarComponent/Header";
import HomePage from "./page/HomePage";
import UserRegister from "./UserComponent/UserRegister";
import UserLoginForm from "./UserComponent/UserLoginForm";
import AdminRegisterForm from "./UserComponent/AdminRegisterForm";
import ViewAllCustomers from "./UserComponent/ViewAllCustomers";
import AddAirplaneForm from "./AirplaneComponent/AddAirplaneForm";
import AddAirportForm from "./AirportComponent/AddAirportForm";
import ViewAllAirplane from "./AirplaneComponent/ViewAllAirplane";
import ViewAllAirport from "./AirportComponent/ViewAllAirport";
import AddFlightForm from "./FlightComponent/AddFlightForm";
import ViewAllFlight from "./FlightComponent/ViewAllFlight";
import UpdateFlightStatusForm from "./FlightComponent/UpdateFlightStatusForm";
import BookFlight from "./FlightBookingComponent/BookFlight";
import MyWallet from "./UserComponent/MyWallet";
import ViewAllFlightBooking from "./FlightBookingComponent/ViewAllFlightBooking";
import ViewPassengerFlightBooking from "./FlightBookingComponent/ViewPassengerFlightBooking";
import ViewScheduledFlightBookings from "./FlightBookingComponent/ViewScheduledFlightBookings";

function App() {
  return (
    <div>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/home/all/hotel/location" element={<HomePage />} />
        <Route path="contact" element={<ContactUs />} />
        <Route path="about" element={<AboutUs />} />
        <Route path="/user/passenger/register" element={<UserRegister />} />
        <Route path="/user/login" element={<UserLoginForm />} />
        <Route path="/user/admin/register" element={<AdminRegisterForm />} />
        <Route path="/admin/all/passenger" element={<ViewAllCustomers />} />
        <Route path="/admin/airplane/add" element={<AddAirplaneForm />} />
        <Route path="/admin/airport/add" element={<AddAirportForm />} />
        <Route path="/admin/airplane/all" element={<ViewAllAirplane />} />
        <Route path="/admin/airport/all" element={<ViewAllAirport />} />
        <Route path="/admin/flight/add" element={<AddFlightForm />} />
        <Route path="/admin/flight/all" element={<ViewAllFlight />} />
        <Route path="/view/flight/all" element={<ViewAllFlight />} />
        <Route
          path="/admin/flight/status/update"
          element={<UpdateFlightStatusForm />}
        />
        <Route path="/passenger/flight/book" element={<BookFlight />} />
        <Route path="/passenger/wallet" element={<MyWallet />} />
        <Route
          path="/admin/flight/booking/all"
          element={<ViewAllFlightBooking />}
        />

        <Route
          path="/passenger/flight/booking/all"
          element={<ViewPassengerFlightBooking />}
        />

        <Route
          path="/flight/booking/status/"
          element={<ViewScheduledFlightBookings />}
        />
      </Routes>
    </div>
  );
}

export default App;
