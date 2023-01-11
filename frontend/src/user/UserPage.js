import {useEffect, useState} from "react";
import {useNavigate} from "react-router";
import {Modal, Navbar} from "react-bootstrap";
import {Menu, MenuItem, ProSidebar, SubMenu} from "react-pro-sidebar";
import DisplayCustomPage from "../admin/components/DisplayCustomPage";
import SockJSClient from 'react-stomp';
import Popup from "reactjs-popup";

function UserPage(){
    const [selection,setSelection] = useState(0);
    const [message,setMessage] = useState("");
    const [alert,setAlert] = useState(false);
    let navigate = useNavigate();


    const logOut = () => {
        localStorage.removeItem('token');
        navigate('/')
    }

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
    }, [])

    const checkUser = () => {

    }

    const renderPage = () => {
        if(selection === 0){
            return <DisplayCustomPage message={"User Page"}/>
        }
    }

    const closeModal = () => {
        setAlert(false);
        console.log(alert);
    }

    const checkCurrentUser = (userId) => {
        fetch('http://localhost:8080/findCurrentUser/' + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => response.json())
            .then(data => {
                console.log(data);
                if(data === true || message.deviceName !== null){
                    setAlert(true);
                }
            })
    }

    return(
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
                    <SubMenu title="Device">
                        <MenuItem onClick={() => navigate("/user/myDevices")}> View</MenuItem>
                    </SubMenu>
                    <MenuItem onClick={() => navigate("/user/support")}> Support</MenuItem>
                </Menu>
            </ProSidebar>

            <SockJSClient
                url="http://localhost:8080/ws"
                topics={['/topic']}
                onConnect={() => {
                    console.log("connected");
                }}
                onMessage={(msg) => {
                    setMessage(msg);
                    checkCurrentUser(msg.userId);
                    console.log(msg);
                }}
                debug = {false}
            />

            <div className="App" style={{fontFamily:"poppins"}}>
                <Modal
                    style={{
                        position: "absolute",
                        top: "80%",
                        left: "47%",
                    }}
                    show={alert}
                    onHide={() => setAlert(false)}
                    open={alert}
                    onClose={() => setAlert(false)}>
                    <div className="myPopupAlert">
                        Device exceeded consumption limit !
                        <br/><br/>
                        <div>
                            <div style={{color:"red"}}>
                            {message.deviceName}
                            </div>
                            has exceeded the consumption limit!
                        </div>
                        <br/><br/>
                        <button className="myButtonModal" onClick={closeModal}>
                            OK
                        </button>
                    </div>

                </Modal>
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

export default UserPage