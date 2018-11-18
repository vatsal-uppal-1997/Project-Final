import React, {Component} from 'react';
import 'bulma';
import axios from 'axios';
import {
  Container,
  Field,
  Label,
  Control,
  Input,
  Help,
  Button
} from 'bloomer';

class RegisterForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      email: '',
      mobile: '',
      locality: '',
      confirm: ''
    };
    this.email = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    this.mobile = /^\d{10}$/;
    this.checkUser = this.checkUser.bind(this);
    this.checkEmail = this.checkEmail.bind(this);
    this.checkPassword = this.checkPassword.bind(this);
    this.setUser = this.setUser.bind(this);
    this.setConfirm = this.setConfirm.bind(this);
    this.setPassword = this.setPassword.bind(this);
    this.setEmail = this.setEmail.bind(this);
    this.setMobile = this.setMobile.bind(this);
    this.setLocality = this.setLocality.bind(this);
    this.submitRegister = this.submitRegister.bind(this);
  }

  checkUser() {
    if (this.state.username === '')
      return <Help isColor='danger'>Username cannot be empty</Help>;
    }
  checkEmail() {
    if (!this.email.test(this.state.email))
      return <Help isColor='danger'>Invalid E-Mail Address</Help>;
    }
  checkMobile() {
    if (!this.mobile.test(this.state.mobile))
      return <Help isColor='danger'>Invalid Mobile Number</Help>;
    }
  checkLocality() {
    if (this.state.locality === '')
      return false;
    else
      return true;
    }
  checkPassword(check) {
    switch (check) {
      case "pass":
        if (this.state.password.length < 8)
          return <Help isColor='danger'>Password must be atleast 8 digits long</Help>;
        else
          return '';
        case "confirm":
        if (this.state.confirm === this.state.password && this.state.password.length >= 8) {
          return true;
        } else {
          return false;
        }
      default:
        return '';
    }
  }
  setUser(event) {
    this.setState({username: event.target.value});
  }
  setEmail(event) {
    this.setState({email: event.target.value});
  }
  setMobile(event) {
    this.setState({mobile: event.target.value});
  }
  setLocality(event) {
    this.setState({locality: event.target.value});
  }
  setPassword(event) {
    this.setState({password: event.target.value});
  }
  setConfirm(event) {
    this.setState({confirm: event.target.value});
  }
  submitRegister(event) {
    if (Object.values(this.state).indexOf('') >= 0)
      return;
    const params = new URLSearchParams();
    params.append("json", JSON.stringify(this.state));
    axios.put("Landing", params).then(res => {
      if (res.data.message === "Account Created")
        this.props.login(this.state.username, res.data.uid);
      else
        alert(res.data.message);
      }
    );
  }

  render() {
    return (<Container>
      <form>
        <Field>
          <Label>Username</Label>
          <Control>
            <Input type="text" placeholder="Username" onChange={this.setUser}/>
          </Control>
          {this.checkUser()}
        </Field>
        <Field>
          <Label>Email</Label>
          <Control>
            <Input type="email" placeholder="Email" onChange={this.setEmail}/>
          </Control>
          {this.checkEmail()}
        </Field>
        <Field>
          <Label>Mobile</Label>
          <Control>
            <Input type="tel" placeholder="Mobile Number" onChange={this.setMobile}/>
          </Control>
          {this.checkMobile()}
        </Field>
        <Field>
          <Label>Locality</Label>
          <Control>
            <Input type="text" placeholder="Locality" onChange={this.setLocality}/>
          </Control>
          {
            !this.checkLocality() && <Help isColor='danger'>Locality can't be empty !</Help>
          }
          </Field>
          <Field>
            <Label>Password</Label>
            <Control>
              <Input type="password" placeholder="Password" onChange={this.setPassword}/>
            </Control>
            {this.checkPassword("pass")}</Field>
            <Field>
              <Label>Confirm Password</Label>
              <Control>
                <Input type="password" placeholder="Confirm Password" onChange={this.setConfirm}/>
              </Control>
            {
            !this.checkPassword("confirm") && <Help isColor='danger'>Password does not match !</Help>
          }
          </Field>
        <Field>
          <Control>
            <Button isColor='primary' onClick={this.submitRegister}>Submit</Button>
          </Control>
        </Field>
      </form>
    </Container>);
  }
}

export default RegisterForm;
