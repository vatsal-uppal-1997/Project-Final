import React, {
  Component
} from 'react';
import 'bulma';
import {
  Navbar,
  NavbarItem,
  NavbarBrand,
  NavbarMenu,
  NavbarEnd,
  NavbarBurger
}
from 'bloomer';


class Nav extends Component {
  render() {
    return (<Navbar>
      <NavbarBrand>
        <NavbarItem>
          Easy Rentals
        </NavbarItem>
        <NavbarBurger isActive={this.props.isActive} onClick={this.props.handler}/>
      </NavbarBrand>
      <NavbarMenu isActive={this.props.isActive} onClick={this.props.handler}>
        <NavbarEnd>
          <NavbarItem>
            <a href="#/">About Us</a>
          </NavbarItem>
        </NavbarEnd>
      </NavbarMenu>
    </Navbar>);
  }
}

export default Nav;
