import React, {useEffect} from 'react';
import ReactTable from "react-table-6";
import "react-table-6/react-table.css"
import {Link} from "react-router-dom";
import {useNavigate} from "react-router";


export default function SupportPage() {
    let navigate = useNavigate();
    const [props, setProps] = React.useState([]);
    const [users, setUsers] = React.useState([]);

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
    }, [])


    useEffect(() => {
        fetch('http://localhost:8080/getAdmins', {
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
            Header: 'Action',
            accessor: 'action',
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
                        Support staff
                        <br/> <br/>
                    </div>

                    <ReactTable
                        data={users}
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