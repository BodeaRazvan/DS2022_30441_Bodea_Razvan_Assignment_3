import {useLocation, useNavigate} from "react-router";
import React, {useEffect} from "react";

export default function EditUserPage(){
    let navigate = useNavigate();
    const location = useLocation();
    const {user} = location.state;

    const [role, setRole] = React.useState(location.state.user.role);
    const [username, setUsername] = React.useState(location.state.user.username);
    const [email, setEmail] = React.useState(location.state.user.email);
    const [address, setAddress] = React.useState(location.state.user.address);


    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
        if (localStorage.getItem('role') !== 'ADMIN') {
            navigate('/')
            localStorage.clear()
        }
    }, [])

    const editUser = () => {
        fetch(`http://localhost:8080/editUser/`,{
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify({
                id: location.state.user.id,
                username: username,
                email: email,
                address: address,
                role: role
            })
        }).then(() => {
            window.location.reload()
        })
    }

    return(
        <div className="App" style={{fontFamily:"poppins"}}>
            <header className="myHeader">
                <div>
                    <div style={{font: "bold", fontSize: "50px"}}>
                        Edit User
                        <br/>  <br/>
                    </div>
                    <form>
                        <label>
                            Username:
                            <br/>
                            <input onChange={(e) => setUsername(e.target.value)} type="text" name="username" placeholder={user.username}/>
                        </label>
                        <br/>
                        <label>
                            Email:
                            <br/>
                            <input onChange={(e) => setEmail(e.target.value)} type="text" name="email" placeholder={user.email}/>
                        </label>
                        <br/>
                        <label>
                            Address:
                            <br/>
                            <input onChange={(e) => setAddress(e.target.value)} type="text" name="address" placeholder={user.address}/>
                        </label>
                        <br/>
                        <label>
                            Role:
                            <br/>
                            <select style={{fontSize: "25px"}} name="roles" id="roles"
                                    onChange={(e) => setRole(e.target.value)}>
                                <option value="USER">USER</option>
                                <option value="ADMIN">ADMIN</option>
                            </select>
                        </label>
                        <br/><br/>
                        <button className="myButton" onClick={editUser}> Edit </button>
                    </form>
                </div>
            </header>
        </div>
    )
}