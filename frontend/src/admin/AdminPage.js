import {useNavigate} from "react-router";
import {useEffect, useState} from "react";
import '../App.css';
import '../index.css';
import {Navbar} from "react-bootstrap";
import {ProSidebar, Menu, MenuItem, SubMenu} from 'react-pro-sidebar';
import 'react-pro-sidebar/dist/css/styles.css';
import DisplayCustomPage from "./components/DisplayCustomPage";
import CreateUserPage from "./components/CreateUserPage";
import CreateDevicePage from "./components/CreateDevicePage";

function AdminPage() {
    const [selection,setSelection] = useState(0);
    const [users,setUsers] = useState([]);

    let navigate = useNavigate();

    const logOut = () => {
        localStorage.removeItem('token');
        navigate('/')
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
                setUsers(data);
                console.log(data);
            })
    },[])

    const renderPage = () => {
        if(selection === 0){
            return <DisplayCustomPage message={"Admin Page"}/>
        }
        if(selection === 1){
            return <CreateUserPage/>
        }
        if(selection === 2){
            return <CreateDevicePage/>
        }
    }

    return (
        <div>
            <Navbar sticky="top" className="navbar" style={{fontFamily: "poppins"}}>
                <div>
                    <p className="navBarTitle" style={{display: "inline", color: "white"}}>EnergX</p>
                    <button className="navbarButton" onClick={logOut}> Log out</button>
                </div>

            </Navbar>
            <ProSidebar style={{
                marginTop: 48,
                position: "fixed",
                fontFamily: "poppins",
                sticky: "top",
                width: "150px",
                minWidth: "150px",
                fontSize: "20px",
            }} className="mySideBar">
                <Menu iconShape="square">
                    <MenuItem onClick={() => setSelection(0)}>Menu</MenuItem>
                    <SubMenu title="User">
                        <MenuItem onClick={() => navigate("/admin/users")}> View</MenuItem>
                        <MenuItem onClick={() => setSelection(1)}> Create</MenuItem>
                    </SubMenu>
                    <SubMenu title="Device">
                        <MenuItem onClick={() => navigate("/admin/devices")}> View</MenuItem>
                        <MenuItem onClick={() => setSelection(2)}> Create</MenuItem>
                    </SubMenu>
                </Menu>
            </ProSidebar>
            <div className="App" style={{fontFamily:"poppins"}}>
                <header className="myHeader">
                    {
                        renderPage()
                    }
                </header>
            </div>
            <header className="myHeader3">
            </header>
        </div>
    )
}


export default AdminPage