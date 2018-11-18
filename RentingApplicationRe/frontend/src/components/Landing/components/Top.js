import React, {
  Component
} from 'react';
import 'bulma';
import Nav from './Nav';
import {
  Container,
  Hero,
  HeroHeader,
  HeroFooter,
  HeroBody,
  Tab,
  Tabs,
  Title,
  TabList,
  TabLink
}
from 'bloomer';

class Top extends Component {
  render() {
  return (<Hero isColor='info' isSize='medium' style={{"background-color":"#0b409c"}}>
    <HeroHeader>
      <Nav isActive={this.props.isActive} handler={this.props.handler}/>
    </HeroHeader>
    <HeroBody>
      <Container hasTextAlign='centered'>
        <Title>Easy Rentals - A one stop solution for all your renting problems</Title>
      </Container>
    </HeroBody>
    <HeroFooter>
      <Tabs isBoxed="isBoxed" isFullWidth="isFullWidth">
        <Container>
          <TabList>
            <Tab isActive={this.props.Login} onClick={(e) => this.props.toggle("login", e)}>
              <TabLink>Login</TabLink>
            </Tab>
            <Tab isActive={!this.props.Login} onClick={(e) => this.props.toggle("register", e)}>
              <TabLink>Register</TabLink>
            </Tab>
          </TabList>
        </Container>
      </Tabs>
    </HeroFooter>
  </Hero>);
  }
}

export default Top;
