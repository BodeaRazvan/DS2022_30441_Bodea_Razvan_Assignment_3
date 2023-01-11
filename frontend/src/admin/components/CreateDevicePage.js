import React from 'react';

export default function CreateDevicePage() {
    const [name, setName] = React.useState("");
    const [description, setDescription] = React.useState("");
    const [address, setAddress] = React.useState("");
    const [maxConsumption, setMaxConsumption] = React.useState(0);

    const createDevice = () => {
        fetch('http://localhost:8080/createDevice', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify({
                name: name,
                description: description,
                address: address,
                maxConsumption: maxConsumption
            })
        }).then((data) => {
            window.location.reload();
        })
    }

    return (
        <div>
            <label> Name:</label>
            <br/>
            <input
                type="text"
                onChange={(e) => setName(e.target.value)}
                id="name"
            />
            <br/>
            <label> Description:</label>
            <br/>
            <input
                type="text"
                onChange={(e) => setDescription(e.target.value)}
                id="description"
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
            <label> Max consumption(KW/h):</label>
            <br/>
            <input
                type="number"
                min={0}
                defaultValue={0}
                onChange={(e) => setMaxConsumption(e.target.value)}
                id="maxConsumption"
            />
            <br/>
            <br/>
            <button className="myButton" onClick={createDevice}> Create device </button>
        </div>
    )
}
