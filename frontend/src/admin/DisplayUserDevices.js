import {useLocation, useNavigate} from "react-router";
import {useEffect} from "react";

export default function DisplayUserDevices(){
    let navigate = useNavigate();
    const location = useLocation();

    const {devices} = location.state;

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
        if (localStorage.getItem('role') !== 'ADMIN') {
            navigate('/')
            localStorage.clear()
        }
    }, [])

    return(
        <div className="App" style={{fontFamily:"poppins"}}>
            <header className="myHeader">
                <div>
                    <div style={{font: "bold", fontSize: "50px"}}>
                        {devices[0].user.username}'s Devices
                        <br/>  <br/>
                    </div>
                    <ul style={{listStyleType:"none"}}>
                        {devices.map(device =>
                            <li style={{textDecoration:"none"}} key={device.id}>
                                <div>
                                    <p>{device.name}</p>
                                    <p>{device.description}</p>
                                    <p>{device.address}</p>
                                    <p>Max consumption: {device.maxConsumption} KWh</p>
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