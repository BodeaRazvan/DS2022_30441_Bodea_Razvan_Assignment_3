import React, {useEffect} from 'react';
import ReactTable from "react-table-6";
import "react-table-6/react-table.css"
import {Link} from "react-router-dom";
import {useNavigate} from "react-router";


export default function DisplayUsers() {
    let navigate = useNavigate();
    const [props, setProps] = React.useState([]);
    const [users, setUsers] = React.useState([]);

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
        if (localStorage.getItem('role') !== 'ADMIN') {
            navigate('/')
            localStorage.clear()
        }
    }, [])

    const deleteUser = (id) => {
        fetch(`http://localhost:8080/deleteUser/` + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(() => {
            window.location.reload();
        })
    }

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
        fetch('http://localhost:8080/getUsers', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => response.json())
            .then(data => {
                console.log(data);
                setUsers(data);
            })
    }, [])


    const columns = [{
        Header: 'Username',
        accessor: 'username'
    },
        {
            Header: 'Email',
            accessor: 'email'
        },
        {
            Header: 'Address',
            accessor: 'address'
        },
        {
            Header: 'Role',
            accessor: 'role'
        },
        {
            Header: 'Devices',
            accessor: 'devices',
            Cell: props => {
                if (props.original.devices.length === 0) {
                    return <div> --- </div>
                }
                return (
                    <Link
                        to="/admin/userDevices"
                        style={{textDecoration: "none"}}
                        state={{devices: props.original.devices}}
                    >
                        <button className="navbarButton2" onClick={() => setProps(props.original)}> View</button>
                    </Link>)
            }
        },
        {
            Header: 'Action',
            accessor: 'action',
            Cell: props =>
                <div>
                    <Link
                        to="/admin/editUser"
                        style={{textDecoration: "none"}}
                        state={{user: props.original}}
                    >
                    <button className="navbarButton2"> Edit</button>
                    </Link>
                    <button className="navbarButtonRed" onClick={() => deleteUser(props.original.id)}> Delete</button>
                </div>
        },
        {
            Header: 'Chat',
            accessor: 'chat',
            Cell: props =>
                <div>
                    <Link
                        to="/messagePage"
                        style={{textDecoration: "none"}}
                        state={{user: props.original}}
                    >
                        <button className="navbarButtonGreen"> Chat </button>
                    </Link>
                </div>
        }
    ]

    return (
        <div className="App" style={{fontFamily: "poppins"}}>
            <header className="myHeader">
                <div>
                    <div style={{font: "bold", fontSize: "50px"}}>
                        Users
                        <br/> <br/>
                    </div>

                    <ReactTable
                        data={users}
                        columns={columns}
                        pageSize={10}
                        rowsPerPageOptions={[10]}
                        style={{font: "bold", fontSize: "30px", textAlign: "center", width: "1800px"}}
                    />

                </div>
            </header>
        </div>
    )
}