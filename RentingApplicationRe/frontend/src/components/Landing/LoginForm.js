import React, {Component} from 'react';
import 'bulma';
import axios from 'axios';
import {
  Container,
  Field,
  Label,
  Control,
  Input,
  Button
} from 'bloomer';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: ''
    };
    this.setUser = this.setUser.bind(this);
    this.setPassword = this.setPassword.bind(this);
    this.submitLogin = this.submitLogin.bind(this);
  }

  setUser(event) {
    this.setState({username: event.target.value});
  }
  setPassword(event) {
    this.setState({password: event.target.value});
  }
  submitLogin(event) {
    if (Object.values(this.state).indexOf('') >= 0)
      return;
    const params = new URLSearchParams();
    params.append("json", JSON.stringify(this.state));
    axios.post("Landing", params).then(res => {
      if (res.data.message === "User Verified")
        this.props.login(this.state.username, res.data.uid);
      }
    );
  }
  render() {
    return (<Container>
      <form>
        <Field>
          <Label>Username</Label>
          <Control>
            <Input type="text" name="username" placeholder="Username" onChange={this.setUser}/>
          </Control>
        </Field>
        <Field>
          <Label>Password</Label>
          <Control>
            <Input type="password" name="password" placeholder="Password" onChange={this.setPassword}/>
          </Control>
        </Field>
        <Field>
          <Control>
            <Button isColor='primary' onClick={this.submitLogin}>Submit</Button>
          </Control>
        </Field>
      </form>
    </Container>);
  }
}

export default Login;
