import React, {useState} from 'react';

export default function CreateUserPage(){
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [password1, setPassword1] = useState('');
    const [email, setEmail] = useState('');
    const [address, setAddress] = useState('');
    const [role, setRole] = useState("USER");

    const createUser = () => {
        fetch('http://localhost:8080/createUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify({
                username: username,
                password1: password,
                password2: password1,
                email: email,
                address: address,
                role: role
            })
        }).then((data) => {
            window.location.reload();
        })
    }

    return(
        <div>
            <label> Username:</label>
            <br/>
            <input
                type="text"
                onChange={(e) => setUsername(e.target.value)}
                id="username"
            />
            <br/>
            <label> Email:</label>
            <br/>
            <input
                type="text"
                onChange={(e) => setEmail(e.target.value)}
                id="email"
            />
            <br/>
            <label> Password:</label>
            <br/>
            <input
                type="password"
                onChange={(e) => setPassword(e.target.value)}
                id="password"
            />
            <br/>
            <label> Repeat password:</label>
            <br/>
            <input
                type="password"
                onChange={(e) => setPassword1(e.target.value)}
                id="password1"
            />
            <br/>
            <label> Address:</label>
            <br/>
            <input
                type="text"
                onChange={(e) => setAddress(e.target.value)}
                id="address"
            />
            <br/>
            <label> Role:</label>
            <br/>
            <select style={{fontSize: "25px"}} name="roles" id="roles"
            onChange={(e) => setRole(e.target.value)}>
                <option value="USER">USER</option>
                <option value="ADMIN">ADMIN</option>
            </select>
            <br/>
            <p/>
            <button className="myButton" onClick={createUser}> Create </button>
            <p/>
        </div>
    )
}