import '../App.css';
import '../index.css';
import {Link} from "react-router-dom";
import {Navbar} from "react-bootstrap";
import {useNavigate} from "react-router";
import React from "react";


function HomePage() {
    let navigate = useNavigate();
    const goToLogin = () => {
        navigate('/login');
    }
    const goToRegister = () => {
        navigate('/register');
    }
    return(
        <div>
            <Navbar sticky="top" className="navbar" style={{fontFamily:"poppins"}}>
                <div>
                    <button className="navbarButton" onClick={goToLogin}> Login </button>      <span> &nbsp; </span>
                    <button className="navbarButton" onClick={goToRegister}> Register</button>
                </div>
            </Navbar>
            <header className="myHeader" style={{fontFamily:"poppins"}}>
                <h1> EnergX </h1>
                <div className="App">
                    The best only Energy Utility Platform
                </div>
                <p style={{marginBottom:"1.5em"}}/>
                <div>
                    <Link to="/login" style={{textDecoration:"none", display:"inline"}} >
                        <button className={"myButton"}> Login </button>
                    </Link>
                    <Link to="/register" style={{textDecoration:"none",display:"inline",marginLeft:"100px"}}>
                        <button className={"myButton"}> Register </button>
                    </Link>
                </div>
                <p style={{marginBottom:"5em"}}/>

            </header>

            <div className="App" >
                <header className="myHeader2" style={{fontFamily:"poppins"}}>

                </header>
            </div>
        </div>
    );
}

export default HomePage;