import {
  BrowserRouter as Router,
  Routes,
  Route,
  //Link
} from "react-router-dom";
import React from "react";
import './App.css';
import LoginPage from "./authentication/LoginPage";
import RegisterPage from "./authentication/RegisterPage";
import UserPage from "./user/UserPage";
import HomePage from "./mainPage/HomePage";
import AdminPage from "./admin/AdminPage";
import DisplayUsers from "./admin/DisplayUsers";
import DisplayDevices from "./admin/DisplayDevices";
import LinkDevicePage from "./admin/LinkDevicePage";
import DisplayUserDevices from "./admin/DisplayUserDevices";
import EditUserPage from "./admin/EditUserPage";
import DisplayMyDevices from "./user/DisplayMyDevices";
import DisplayMeasurements from "./user/DisplayMeasurements";
import SupportPage from "./user/SupportPage";
import MessagePage from "./MessagePage";
function App() {
  return (
      <Router>
        <Routes>
            <Route path="/" element={<HomePage/>} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage/>} />
            <Route path="/user" element={<UserPage />}/>
            <Route path="/admin" element={<AdminPage />}/>
            <Route path="/admin/users" element={<DisplayUsers />}/>
            <Route path="/admin/devices" element={<DisplayDevices/>}/>
            <Route path="/admin/linkDevice" element={<LinkDevicePage/>}/>
            <Route path="/admin/userDevices" element={<DisplayUserDevices/>}/>
            <Route path="/admin/editUser" element={<EditUserPage/>}/>
            <Route path="/user/myDevices" element={<DisplayMyDevices/>}/>
            <Route path="/user/displayMeasurements" element={<DisplayMeasurements/>}/>
            <Route path="/user/support" element={<SupportPage/>}/>
            <Route path="/messagePage" element={<MessagePage/>}/>
        </Routes>
      </Router>
  );
}

export default App;
