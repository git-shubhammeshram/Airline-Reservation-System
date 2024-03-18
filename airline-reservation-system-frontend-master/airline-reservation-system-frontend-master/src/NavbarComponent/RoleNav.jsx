import AdminHeader from "./AdminHeader";
import NormalHeader from "./NormalHeader";
import CustomerHeader from "./CustomerHeader";

const RoleNav = () => {
  const passenger = JSON.parse(sessionStorage.getItem("active-passenger"));
  const admin = JSON.parse(sessionStorage.getItem("active-admin"));

  if (admin != null) {
    return <AdminHeader />;
  } else if (passenger != null) {
    return <CustomerHeader />;
  } else {
    return <NormalHeader />;
  }
};

export default RoleNav;
