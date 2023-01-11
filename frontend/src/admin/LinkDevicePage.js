import {useLocation, useNavigate} from "react-router";
import React, {useEffect} from "react";
import {Link} from "react-router-dom";

export default function LinkDevicePage(){
    let navigate = useNavigate();
    const location = useLocation();

    const {device} = location.state;
    const {users} = location.state;

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
        if (localStorage.getItem('role') !== 'ADMIN') {
            navigate('/')
            localStorage.clear()
        }
    }, [])

    const linkDeviceToUser = (id) => {
        fetch('http://localhost:8080/linkDevice/' + device.id + '/' + id,{
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(() => {
            navigate('/admin/devices');
            })
    }




    return (
        <div className="App" style={{fontFamily:"poppins"}}>
            <header className="myHeader">
                <div>
                    <div style={{font: "bold", fontSize: "50px"}}>
                        Select the user to link the device to
                        <br/>  <br/>
                        <br/>
                    </div>
                    <ul style={{listStyleType:"none"}}>
                        {users.map(user =>
                            <li style={{textDecoration:"none"}} key={user.id}>
                                <div>
                                    <Link style={{textDecoration:"none"}}  onClick={(e) =>{e.preventDefault(); linkDeviceToUser(user.id)}}>
                                    <p>{user.username}</p>
                                    <p>{user.email}</p>
                                    <p>{user.address}</p>
                                    <p>{user.role}</p>
                                    </Link>
                                    <br/>
                                </div>
                            </li>
                        )}
                    </ul>
                </div>
            </header>
        </div>
    )

}