import './App.css';
import React, {useEffect, useLayoutEffect , useRef, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import {login as loginAction, runLogOutTimer, saveTokenToLocalStorage} from "./store/auth"
import {useLocation} from "react-router";
import SockJSClient from "react-stomp";

function MessagePage(){
    const location = useLocation();
    let navigate = useNavigate();
    const user = location.state.user;

    const[isTyping,setIsTyping] = useState(false);
    const[wsMessage,setWsMessage] = useState("")
    const [hasSeen,setHasSeen] = useState(false);

    const [message, setMessage] = useState('');
    const [messageArray, setMessageArray] = useState([]);

    const divRef = useRef(null);
    const bottomRef = useRef(null);

    const sortedMessages = [...messageArray].sort((a, b) => a.id - b.id);

    const scrollBottom = () =>{
        bottomRef.current.scrollIntoView({ behavior: 'smooth' })
    }
    useEffect(() => {
       scrollBottom()
    }, [messageArray]);

    useEffect(() => {
        if (localStorage.getItem('token') === null) {
            navigate('/');
        }
    }, [])

    const checkMessage = (msg) => {
        console.log(msg)
        if (msg.hasOwnProperty('from') && !msg.hasOwnProperty('isTyping') && !msg.hasOwnProperty('hasSeen')) {
            console.log("Has property from")
            checkCurrentUserAndReload(msg.to)
        }
        if(msg.hasOwnProperty('isTyping') && msg.from===user.username){
            console.log("Has property isTyping")
            checkCurrentUserNotify(msg.userId)
        }
        if(msg.hasOwnProperty('hasSeen') && msg.from===user.username){
            console.log("Has property hasSeen")
            checkCurrentUserSeen(msg.userId)
        }
    }

    const checkCurrentUserAndReload = (userId) => {
        fetch('http://localhost:8080/findCurrentUser/' + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => response.json())
            .then(data => {
                console.log(data);
                if(data === true){
                    setWsMessage('')
                    window.location.reload();
                }
            })
    }

    const checkCurrentUserNotify = (userId) => {
        fetch('http://localhost:8080/findCurrentUser/' + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => response.json())
            .then(data => {
                console.log(data);
               if(data===true){
                   setIsTyping(true)
                   scrollBottom()
                   setTimeout(() => {
                       setIsTyping(false)
                   },1000)
               }
            })
    }

    const checkCurrentUserSeen = (userId) => {
        fetch('http://localhost:8080/findCurrentUser/' + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => response.json())
            .then(data => {
                console.log(data);
                if(data===true){
                   setHasSeen(true)
                    setTimeout(() => {
                        scrollBottom()
                    },10)
                }
            })
    }

    useEffect(() => {
        fetch('http://localhost:8080/getMessages/' + user.id, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(result => result.json())
            .then(data => {
                setMessageArray(data);
                scrollBottom();
            })
    }, [])

    const sendMessage = () => {
        setHasSeen(false)
       fetch('http://localhost:8080/sendMessage', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json',
               'Authorization': localStorage.getItem('token')
           },
           body: JSON.stringify({
               message: message,
               userId: user.id
           })
       }).then(() => {
           setMessage('');
           console.log('Message sent');
           window.location.reload();
           scrollBottom()
       })
    }

    const notifyIsTyping = () =>{
        fetch('http://localhost:8080/notifyIsTyping/' + user.id,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(() => {})
    }

    const notifyHasSeen = () =>{
        fetch('http://localhost:8080/notifyHasSeen/' + user.id,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(() => {})
    }



    return(
        <div>
            <SockJSClient
                url="http://localhost:8080/ws"
                topics={['/message']}
                onConnect={() => {
                    console.log("connected");
                }}
                onMessage={(msg) => {
                    setWsMessage(msg);
                    checkMessage(msg);
                }}
                debug = {false}
            />

            <div className="App">
                <header className="myHeader5">
                    <div className="topAlignTitle">
                        Chatting with {user.username}
                    </div>
                    <div onMouseEnter={() => notifyHasSeen()} ref={divRef} className="topAlign">
                        {
                            sortedMessages.map((message, index) => {
                                return (
                                    <div key={index}>
                                        <div>
                                            {user.id === message.receiverId ?
                                                <div className="messageSender">
                                                    <p style={{fontSize:"9px"}}>{message.timestamp}</p>
                                                    {message.message}</div>
                                                :
                                                <div className="messageReceiver">
                                                    <p style={{fontSize:"9px"}}>{message.timestamp}</p>
                                                    {message.message}</div>}
                                        </div>
                                    </div>
                                )
                            }, [])
                        }
                        {hasSeen && <div className="messageTyping">Seen</div>}
                        {isTyping ? <div className="messageTyping">{user.username} is typing...</div> : null}
                        <div ref={bottomRef} />
                    </div>
                    <div className="bottomAlign">
                        <input
                            style={{
                                width:"50%",
                                height:"50px",
                                borderRadius:"10px",
                                border:"none",
                                paddingLeft:"10px",
                                fontSize:"20px",
                            }}
                            value={message}
                            type="text"
                            onChange={(e) => {setMessage(e.target.value); notifyIsTyping()}}
                            id="messageField"
                        />
                        <p/>
                        <button className="myButton"  onClick={sendMessage}> Send </button>
                    </div>
                </header>
            </div>
        </div>
    )
}

export default MessagePage;