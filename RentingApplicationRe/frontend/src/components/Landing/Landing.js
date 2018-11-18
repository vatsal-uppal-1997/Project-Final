import React, { Component } from 'react';
import 'bulma';
import {Section} from 'bloomer';
import Login from './LoginForm';
import Register from './RegisterForm';
import Top from './components/Top';

class Landing extends Component {
  constructor(props) {
    super(props);
    this.state = { isActive : false, Login: true, Register: false };
    this.onClickNav = this.onClickNav.bind(this);
    this.toggle = this.toggle.bind(this);
    this.loginOrRegister = this.loginOrRegister.bind(this);
  }

  onClickNav() {
    this.setState((currentState) => ({
      isActive : !currentState.isActive,
      Login: true
    }));
  }
  toggle(name, event) {
    console.log("called");
    if (name === "login") {
      this.setState({
        Login: true,
        Register: false
      });
    } else {
      this.setState({
        Login: false,
        Register: true
      });
    }
  }
  loginOrRegister() {
    if (this.state.Login)
      return <Login login = {this.props.login}/>;
    else if (this.state.Register)
      return <Register login = {this.props.login}/>;
  }
  render() {
    return (
      <div>
        <Top isActive = {this.state.isActive} handler = {this.onClickNav} toggle = {this.toggle} Login = {this.state.Login}/>
        <Section>
          {this.loginOrRegister()}
        </Section>
      </div>
    );
  }
}

export default Landing;
