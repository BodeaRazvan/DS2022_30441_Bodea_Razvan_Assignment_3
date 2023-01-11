import {useNavigate} from "react-router";
import React, {useEffect} from "react";
import ReactTable from "react-table-6";
import {Link} from "react-router-dom";
import Popup from 'reactjs-popup';

export default function DisplayDevices(){
    let navigate = useNavigate();
    const [users, setUsers] = React.useState([]);
    const [selectedUser, setSelectedUser] = React.useState(null);
    const [props, setProps] = React.useState([]);
    const [devices, setDevices] = React.useState([]);

    const[date, setDate] = React.useState("");
    const[time, setTime] = React.useState("");
    const[energy, setEnergy] = React.useState("");

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
        if (localStorage.getItem('role') !== 'ADMIN') {
            navigate('/')
            localStorage.clear()
        }
    }, [])

    useEffect(() => {
        fetch('http://localhost:8080/getUsers',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => response.json())
            .then(data => {
                setUsers(data);
            })
    }, [])


    useEffect(() => {
        fetch('http://localhost:8080/getDevices', {
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

    const deleteDevice = (id) => {
        fetch(`http://localhost:8080/deleteDevice/` + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(() => {
            window.location.reload();
        })
    }

    const unlinkDevice = (id) => {
        fetch(`http://localhost:8080/unlinkDevice/` + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(() => {
            window.location.reload();
        })
    }

    const addMeasurement = (id) => {
        if(date === "" || time === "" || energy === ""){
            console.log("Please fill all fields");
            return;
        }
        fetch(`http://localhost:8080/addMeasurement/` + id, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify({
                date: date,
                time: time,
                energy: energy
            })
        }).then(() => {
            window.location.reload();
        })
    }



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
            Header: 'Owner',
            accessor: 'user',
            Cell: props =>{
                if(props.original.user === null){
                    return (
                        <div>
                            <Link style={{textDecoration:"none"}} to="/admin/linkDevice" state={{users:users,device:props.original}}>
                            <button className="navbarButtonGreen" > Link </button>
                            </Link>
                        </div>
                    )
                }
                else{
                    return (<div>
                        <div>{props.original.user.username}
                            <button className="navbarButton2" onClick={(e) => unlinkDevice(props.original.id)}> Unlink </button>
                        </div>
                         </div>)
                }
            }},
        {
            Header: 'Measurements',
            accessor: 'measurements',
            Cell: props =>
                <div>
                    <Popup trigger={<button className="navbarButtonGreen"> Add </button>} modal>
                        <div className="myPopup">
                            <label>Date</label>
                            <input
                                type="date"
                                onChange={(e) => setDate(e.target.value)}
                            />
                            <br/>
                            <label>Time</label>
                            <input
                                type="time"
                                onChange={(e) => setTime(e.target.value)}
                            />
                            <br/>
                            <label>Energy KWh</label>
                            <input
                                type="number"
                                onChange={(e) => setEnergy(e.target.value)}
                            />
                            <br/>
                            <button
                                className="navbarButtonGreen2"
                                onClick={(e) => addMeasurement(props.original.id)}
                            > Add </button>
                        </div>
                    </Popup>
                </div>
        },
        {
            Header: 'Delete',
            accessor: 'delete',
            Cell: props => <button className="navbarButtonRed" onClick={(e) => deleteDevice(props.original.id)}> Delete </button>
        }
        ]

    return(
        <div className="App" style={{fontFamily:"poppins"}}>
            <header className="myHeader">
                <div>
                    <div style={{font: "bold", fontSize: "50px"}}>
                        Devices
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