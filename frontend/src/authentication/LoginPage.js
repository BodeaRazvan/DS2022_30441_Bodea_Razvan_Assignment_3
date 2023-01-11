import '../App.css';
import React, {useRef, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import {login as loginAction, runLogOutTimer, saveTokenToLocalStorage} from "../store/auth"
import {Navbar} from "react-bootstrap";

function LoginPage(){
    const dispatch = useDispatch();
    let navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const inputRefference = useRef(null);
    const inputRefference1 = useRef(null);

    const login = () => {
        fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
            .then(response => response.json())
            .then(data => {
                saveTokenToLocalStorage(data.token,data.user.role);
                runLogOutTimer(1000)
                dispatch(loginAction(data))
                if(data.user.role === "USER"){
                    navigate("/user");
                    return;
                }
                else if(data.user.role === "ADMIN"){
                    navigate("/admin");
                    return;
                }
                navigate("/login");
            })
            .catch(error => {
                inputRefference.current.value="";
                inputRefference1.current.value="";
                console.log(error);
            });
    }

    const goToRegister = () => {
        navigate("/register");
    }

    return(
        <div>
            <Navbar sticky="top" className="navbar" style={{fontFamily:"poppins"}}>
                <div>
                    <p className="navBarTitle" style={{display:"inline"}}>Unicard</p>
                    <button className="navbarButton" onClick={goToRegister}> Register</button>
                </div>
            </Navbar>
        <div className="App">
            <header className="myHeader">
                <h1> Login </h1>
                <label> Username:</label>
                <input
                    ref={inputRefference}
                    type="text"
                    onChange={(e) => setUsername(e.target.value)}
                    id="username"
                />
                <label> Password:</label>
                <input
                    ref={inputRefference1}
                    type="password"
                    onChange={(e) => setPassword(e.target.value)}
                    id="password1"
                />
                <p/>
                <button className="myButton"  onClick={login}> Login </button>
                <p/>
            </header>
        </div>
        </div>
    )
}

export default LoginPage;