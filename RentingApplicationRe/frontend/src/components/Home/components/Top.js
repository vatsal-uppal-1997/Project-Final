import React, {Component} from 'react';
import 'bulma';
import {
  Navbar,
  NavbarBrand,
  NavbarBurger,
  NavbarMenu,
  NavbarItem,
  NavbarLink,
  NavbarDivider,
  NavbarDropdown,
  NavbarEnd,
  Field,
  Control,
  Button
} from 'bloomer';

class Top extends Component {

  constructor(props) {
    super(props);
    this.state = {
      isActive: false,
      txt : "Your Listings",
      txt2 : "Interested"
    };
    this.onClickNav = this.onClickNav.bind(this);
    this.changeView = this.changeView.bind(this);
    this.changeView2 = this.changeView2.bind(this);
  }

  changeView() {
    if (this.state.txt === "Your Listings") {
      this.setState({txt : "All Listings"});
    } else {
      this.setState({txt : "Your Listings"});
    }
    this.props.showMy();
  }
  changeView2() {
    if (this.state.txt2 === "Interested")
      this.setState({txt2 : "All Listings"});
    else
      this.setState({txt2 : "Interested"});
    this.props.showInterested();
  }
  onClickNav() {
    this.setState((currentState) => ({
      isActive: !currentState.isActive,
      Login: true
    }));
  }

  render() {
    return (<Navbar>
      <NavbarBrand>
        <NavbarItem>
          Easy Rentals
        </NavbarItem>
        <NavbarBurger isActive={this.state.isActive} onClick={this.onClickNav}/>
      </NavbarBrand>
      <NavbarMenu isActive={this.state.isActive} onClick={this.onClickNav}>
        <NavbarEnd>
          <NavbarItem hasDropdown="hasDropdown" isHoverable="isHoverable">
            <NavbarLink>{this.props.username}</NavbarLink>
            <NavbarDropdown>
              {this.state.txt !== "All Listings" && <NavbarItem href='#/' onClick={this.changeView2}>{this.state.txt2}</NavbarItem>}
              {this.state.txt2 !== "All Listings" && <NavbarItem href='#/' onClick={this.changeView}>{this.state.txt}</NavbarItem>}
              <NavbarDivider/>
              <NavbarItem>
                <Field>
                  <Control>
                    <Button isColor="danger" onClick={this.props.logout}>Logout</Button>
                  </Control>
                </Field>
              </NavbarItem>
            </NavbarDropdown>
          </NavbarItem>
        </NavbarEnd>
      </NavbarMenu>
    </Navbar>);
  }
}
export default Top;
