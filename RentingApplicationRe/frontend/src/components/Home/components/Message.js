import React, {Component} from 'react';
import axios from 'axios';
import {Widget, addResponseMessage, addUserMessage, toggleWidget} from 'react-chat-widget';
import 'react-chat-widget/lib/styles.css';
import openSocket from 'socket.io-client';

class Message extends Component {

    constructor(props) {
        super(props);
        this.state = {
            messages : []
        }
        this.interval = "";
        this.handleNewUserMessage = this.handleNewUserMessage.bind(this);
        //this.listenAndUpdate = this.listenAndUpdate.bind(this);
        //this.onMount = this.onMount.bind(this);
        //this.onMount();
        axios.get("Message");
        this.soc = openSocket("http://"+window.location.hostname+":5010");
        this.soc.on("sendUid", () => {
            let json = JSON.stringify({"uid" : this.props.userInfo.id});
            this.soc.emit("getUid", json);
            this.soc.emit("getAllMessages", json);
        });
        this.soc.on("receiveAllMessages", (data) => {
            console.log(data);
            let messages = JSON.parse(data);
            this.setState({messages : messages});
            messages["messages"].forEach((element) => {
                if (element.type === "Sent") {
                    addUserMessage(element.message);
                } else if (element.type === "Received") {
                    addResponseMessage(element.message);
                }
            });        
        });
        this.soc.on("gotAMessage", (data) => {
            console.log(data);
            addResponseMessage(data);
        });
    }
    componentWillUnmount() {
        this.soc.disconnect();
    } 
    componentDidUpdate() {
        let message = this.props.message;
        if (message.lusername !== undefined & message.ltitle !== undefined) {
            let sendMessage = `Thanks for showing interest in the listing "${message.ltitle}". You can now chat with @${message.lusername} to discuss further.`;
            let dataMessage = {
                userFrom: "SYSTEM",
                userTo: this.props.userInfo.username,
                timestamp: Date.now(),
                message: sendMessage
            }
            this.soc.emit("postAMessage", JSON.stringify(dataMessage));
            //addResponseMessage(sendMessage);
            toggleWidget();
            this.props.unset();
        }
    }
    // onMount() {
    //     this.interval = setInterval(()=>{this.listenAndUpdate()}, 2500);
    //     console.log(this.interval);
    // }


    // listenAndUpdate() {
    //     const params = new URLSearchParams();
    //     params.append("uid", this.props.userInfo.id);
    //     axios.get("Message?" + params.toString()).then(res => {
    //         let msgs = [];
    //         msgs = res.data.messages;
    //         if (this.state.messages.length === 0) {
    //             this.setState({messages : msgs});
    //             this.state.messages.forEach((element) => {
    //                 if (element.type === "Sent") {
    //                     addUserMessage(element.message);
    //                 } else if (element.type === "Received") {
    //                     addResponseMessage(element.message);
    //                 }
    //             });        
    //         } else if (JSON.stringify(msgs) !== JSON.stringify(this.state.messages)) {
    //             let i = 0;
    //             for (i = this.state.messages.length; i < msgs.length; i++) {
    //                 if (msgs[i].type === "Sent") {
    //                     this.setState(prevState => ({
    //                         messages : [...prevState.messages, msgs[i]]
    //                     }));
    //                 } else if (msgs[i].type === "Received") {
    //                     addResponseMessage(msgs[i].message);
    //                     this.setState(prevState => ({
    //                         messages : [...prevState.messages, msgs[i]]
    //                     }));
    //                 }
    //             }
    //         }
    //     });
    // }

    handleNewUserMessage(msg) {
        msg = msg.trim();
        if (!msg.startsWith("@")) {
          addResponseMessage("Invalid message format !");
          addResponseMessage("A message must start with @[some username]");
          return;
        }
        let getUserName = msg.match(/[a-z'\-]+/gi)[0];
        msg = msg.slice(getUserName.length+1).trim();
        let dataMessage = {
            userFrom : this.props.userInfo.username,
            userTo : getUserName,
            timestamp : Date.now(),
            message : msg
        }
        console.log(dataMessage);
        this.soc.emit("postAMessage", JSON.stringify(dataMessage));
        // const params = new URLSearchParams();
        // params.append("username", getUserName);
        // params.append("message", msg);
        // params.append("timestamp", Date.now());
        // axios.post("Message", params);
      }


    
    render() {
        return(<Widget handleNewUserMessage={this.handleNewUserMessage} title="Requests" subtitle="Your Requests appear here !" senderPlaceHolder="@Username Your Message"/>);
    }
}

export default Message;