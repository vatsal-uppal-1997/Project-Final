import React, {Component} from 'react';
import 'bulma';
import {
  Card,
  CardHeader,
  CardHeaderTitle,
  CardFooter,
  CardFooterItem,
  CardImage,
  Image,
  CardContent,
  Content
} from 'bloomer';
import axios from 'axios';

class ListingEntity extends Component {
  constructor(props) {
    super(props);
    this.setText = this.setText.bind(this);
    this.edit = this.edit.bind(this);
    this.delete = this.delete.bind(this);
    this.contact = this.contact.bind(this);
    this.interested = this.interested.bind(this);
    this.remove = this.remove.bind(this);
  }
  edit() {
    let defaultValues = {
      'id' : this.props.lid,
      'title' : this.props.title,
      'locality' : this.props.locality,
      'description' : this.props.description
    }
    console.log("defaultValues "+defaultValues);
    this.props.enableEdit(defaultValues);
  }
  delete() {
    const params = new URLSearchParams();
    params.append("id", this.props.lid);
    params.append("delete", "true");
    axios.delete("Listing?"+params.toString()).then(res => {
      if (res.data.message === "listing deleted")
        this.props.trigger();
    });
  }
  remove() {
    const params = new URLSearchParams();
    params.append("id", this.props.lid);
    params.append("remove", "true");
    axios.delete("Listing?"+params.toString()).then(res => {
      if (res.data.message === "listing removed")
        this.props.trigger();
    });
  }
  contact() {

  }
  interested() {
    const params = new URLSearchParams();
    params.append("uid", this.props.uid);
    params.append("lid", this.props.lid);
    axios.put("Listing?"+params.toString());
  }
  setText() {
    if (this.props.interested !== undefined && this.props.interested === true) {
      return (<CardFooter>
        <CardFooterItem href="#/" onClick={this.contact}>Contact</CardFooterItem>
        <CardFooterItem href="#/" onClick={this.remove}>Remove from Interested</CardFooterItem>
      </CardFooter>);
    }
    if (this.props.uid === this.props.luid) {
      return (<CardFooter>
        <CardFooterItem href="#/" onClick={this.edit}>Edit</CardFooterItem>
        <CardFooterItem href="#/" onClick={this.delete}>Delete</CardFooterItem>
      </CardFooter>);
    } else {
      return (<CardFooter>
        <CardFooterItem href="#/" onClick={this.contact}>Contact</CardFooterItem>
        <CardFooterItem href="#/" onClick={this.interested}>Interested</CardFooterItem>
      </CardFooter>);
    }
  }
  render() {
    return (<Card>
      <CardHeader>
        <CardHeaderTitle>
          {this.props.title}
        </CardHeaderTitle>
      </CardHeader>
      <CardImage>
        <Image isRatio='square' src={this.props.image}/>
      </CardImage>
      <CardContent>
        <Content>
          {this.props.description}
        </Content>
      </CardContent>
      {this.setText()}
    </Card>);
  }
}

export default ListingEntity;
