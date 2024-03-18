import Carousel from "./Carousel";
import Footer from "./Footer";
import { Link } from "react-router-dom";
import travel_1 from "../images/travel_1.png";
import travel_2 from "../images/travel_2.png";

const HomePage = () => {
  return (
    <div className="container-fluid mb-2">
      <Carousel />

      <div className="container mt-5">
        <div className="row">
          <div className="col-md-8">
            <h1 className="text-color">
              Welcome to Airline Reservation System
            </h1>
            <p>
              Welcome to our Airline Reservation System, where seamless travel
              planning and exhilarating adventures meet. Embark on a journey of
              convenience as you navigate our user-friendly platform to explore
              an array of flights, destinations, and class options. Whether
              you're a seasoned traveler or a first-time flyer, our system
              offers you the power to book your flights with ease, ensuring a
              hassle-free experience from takeoff to landing.
            </p>
            <p>
              From the moment you step into our digital gateway, you're one step
              closer to realizing your travel dreams. Join us as we redefine the
              way you book flights, making every adventure a memorable
              destination in itself.
            </p>
            <Link to="/user/login" className="btn bg-color custom-bg-text">
              Get Started
            </Link>
          </div>
          <div className="col-md-4">
            <img
              src={travel_2}
              alt="Logo"
              width="400"
              height="auto"
              className="home-image"
            />
          </div>
        </div>

        <div className="row mt-5">
          <div className="col-md-4">
            <img
              src={travel_1}
              alt="Logo"
              width="400"
              height="auto"
              className="home-image"
            />
          </div>
          <div className="col-md-8">
            <h1 className="text-color ms-5">
              Real-time Availability and Instant Confirmation
            </h1>
            <p className="ms-5">
              Experience the ultimate in convenience with our real-time
              availability and instant confirmation system. Say goodbye to
              uncertainty and waiting – our cutting-edge technology ensures you
              receive up-to-the-minute flight availability and confirmation,
              giving you the confidence to secure your travel plans without
              delay. With just a few clicks, you can explore flight options,
              select your preferred class, and receive immediate confirmation,
              putting you on the fast track to your next adventure.
            </p>
            <p className="ms-5">
              Moreover, our system offers instant confirmation of bookings. Once
              users complete their reservation, they receive an immediate
              confirmation email or notification, assuring them that their
              tickets are secured and ready for travel. This eliminates any
              uncertainty and allows customers to proceed with their travel
              plans with confidence.
            </p>
            <Link to="/user/login" className="btn bg-color custom-bg-text ms-5">
              Get Started
            </Link>
          </div>
        </div>
      </div>
      <hr />
      <Footer />
    </div>
  );
};

export default HomePage;
