import {useLocation, useNavigate} from "react-router";
import React, {useEffect} from "react";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);


export default function DisplayMeasurements(){
    let navigate = useNavigate();
    const location = useLocation();

    const [measurements, setMeasurements] = React.useState([]);

    const {date} = location.state;
    const {deviceId} = location.state;
    const {deviceName} = location.state;

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
    }, [])

    // GRAPH SECTION
    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Measurements',
            },
        },
    };

    const sortedMeasurements = measurements.sort((a, b) => (a.time > b.time) ? 1 : -1);

    const displayedData = sortedMeasurements.map((measurement) => measurement.energy);

    const labels = sortedMeasurements.map((measurement) => measurement.time.slice(0,-3));

     const data = {
        labels,
        datasets: [
            {
                label: "My device",
                data: displayedData,
                borderColor: 'rgb(255, 99, 132)',
                backgroundColor: 'rgba(255, 99, 132, 0.5)',
            }
        ],
    };
    //END GRAPH SECTION

    useEffect(() => {
        fetch('http://localhost:8080/getMeasurements/' + deviceId + '/' + date, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => response.json())
            .then(data => {
                setMeasurements(data);
            })
    }, [])

    return(
        <div className="App" style={{fontFamily:"poppins"}}>
            <header className="myHeader">
                <div>
                    Measurements for {deviceName} on {date}
                    <p/>
                     <Line className="myLine" options={options} data={data} />
                </div>
            </header>
        </div>
    )
}