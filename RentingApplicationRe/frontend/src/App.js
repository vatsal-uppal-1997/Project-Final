import React, {Component} from 'react';
import 'bulma';
import axios from 'axios';
import Landing from './components/Landing/Landing';
import Home from './components/Home/Home';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loggedIn: false,
      uname: "",
      uid: ""
    };
    this.logIn = this.logIn.bind(this);
    this.logOut = this.logOut.bind(this);
    this.isLoggedIn = this.isLoggedIn.bind(this);
    this.checkIfLoggedIn = this.checkIfLoggedIn.bind(this);
    this.checkIfLoggedIn();
  }
  checkIfLoggedIn() {
    axios.get("User").then(res => {
      if (res.data.id === undefined)
        this.setState({loggedIn: false});
      else
        this.logIn(res.data.username, res.data.id);
      }
    );
  }
  isLoggedIn() {
    if (this.state.loggedIn) {
      return <Home logout={this.logOut} uid={this.state.uid}/>;
    }
    return <Landing login={this.logIn}/>;
  }

  logIn(uname, uid) {
    this.setState({loggedIn: true, uname: uname, uid: uid});
  }

  logOut() {
    axios.post("User").then(res => {
      if (res.data.message === "logged out")
        this.setState({loggedIn: false, uname: "", uid: ""});
    });
  }

  render() {
    return (<div>
      {this.isLoggedIn()}
    </div>);
  }
}

export default App;
