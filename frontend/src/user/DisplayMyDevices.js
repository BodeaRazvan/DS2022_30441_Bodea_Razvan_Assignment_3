import {useNavigate} from "react-router";
import React, {useEffect} from "react";
import {Link} from "react-router-dom";
import Popup from "reactjs-popup";
import ReactTable from "react-table-6";


export default function DisplayMyDevices(){
    let navigate = useNavigate();
    const [devices, setDevices] = React.useState([]);
    const [date, setDate] = React.useState("");

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
    }, [])

    useEffect(() => {
        fetch('http://localhost:8080/getMyDevices', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => response.json())
            .then(data => {
                setDevices(data);
            })
    }, [])


    const columns = [{
        Header: 'Name',
        accessor: 'name'
    },
        {
            Header: 'Description',
            accessor: 'description'
        },
        {
            Header: 'Address',
            accessor: 'address'
        },
        {
            Header: 'Max Consumption',
            accessor: 'maxConsumption'
        },
        {
            Header: 'Measurements',
            accessor: 'measurements',
            Cell: props =>
                <div>
                    <Popup trigger={<button className="navbarButtonGreen"> Show </button>} modal>
                        <div className="myPopup">
                            <label>Select Date</label>
                            <input
                                type="date"
                                onChange={(e) => setDate(e.target.value)}
                            />
                            <p/>
                            <Link
                                style={{textDecoration: "none"}}
                                to={"/user/displayMeasurements"}
                                state={{date:date, deviceId:props.original.id, deviceName:props.original.name}}
                            >
                                <button className="navbarButtonGreen2"> Show </button>
                            </Link>
                        </div>
                    </Popup>
                </div>
        }
    ]


    return(
        <div className="App" style={{fontFamily:"poppins"}}>
            <header className="myHeader">
                <div>
                    <div style={{font: "bold", fontSize: "50px"}}>
                        My Devices
                        <br/>  <br/>
                    </div>
                    <ReactTable
                        data={devices}
                        columns={columns}
                        pageSize={10}
                        rowsPerPageOptions={[10]}
                        style={{font: "bold", fontSize: "30px", textAlign: "center", width: "1500px"}}
                    />
                </div>
            </header>
        </div>
    )
}