import React, {Component} from 'react';
import 'bulma';
import {
  Modal,
  ModalBackground,
  ModalCard,
  ModalCardHeader,
  ModalCardTitle,
  Delete,
  ModalCardBody,
  Button,
  Field,
  Label,
  Control,
  Input,
  TextArea
} from 'bloomer';
import axios from 'axios';
class AddListing extends Component {

  constructor(props) {
    super(props);
    this.state = {
      title: "",
      locality: "",
      description: "",
      image: null
    }
    this.handleEvents = this.handleEvents.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.reset = this.reset.bind(this);
  }
  reset() {
    this.setState({title: "", locality: "", description: "", image: null});
  }
  handleEvents(event, name) {
    if (name === "image") {
      this.setState({[name]: event.target.files[0]});
      return;
    }
    this.setState({[name]: event.target.value});
  }
  handleSubmit(event) {
    event.preventDefault();
    const fd = new FormData();
    fd.append("title", this.state.title.trim());
    fd.append("locality", this.state.locality.trim());
    fd.append("description", this.state.description.trim());
    if (this.state.image !== null)
      fd.append("image", this.state.image, this.state.image.name);
    axios.post("Listing", fd).then(res=>{
      if (res.data.message === "Listing Successfully Created") {
          this.props.disable();
      }
    });
  }
  render() {
    return (<Modal isActive={this.props.isActive}>
      <ModalBackground/>
      <ModalCard>
        <ModalCardHeader>
          <ModalCardTitle>Create new listing</ModalCardTitle>
          <Delete onClick={this.props.disable}/>
        </ModalCardHeader>
        <ModalCardBody>
          <form onSubmit={this.handleSubmit}>
            <Field>
              <Label>Title</Label>
              <Control>
                <Input type="text" required="required" onChange={e => this.handleEvents(e, "title")}/>
              </Control>
            </Field>
            <Field>
              <Label>Locality</Label>
              <Control>
                <Input type="text" required="required" onChange={e => this.handleEvents(e, "locality")}/>
              </Control>
            </Field>
            <Field>
              <Label>Description</Label>
              <Control>
                <TextArea required="required" onChange={e => this.handleEvents(e, "description")}/>
              </Control>
            </Field>
            <Field>
              <Label>Image</Label>
              <Control>
                <Input type="file" accept="image/*" onChange={e => this.handleEvents(e, "image")}/>
              </Control>
            </Field>
            <Field isGrouped>
              <Control>
                <Button type="submit" isColor='success'>Save</Button>
              </Control>
              <Control>
                <Button type="reset" isColor='warning' onClick={this.reset}>Cancel</Button>
              </Control>
            </Field>
          </form>
        </ModalCardBody>
      </ModalCard>
    </Modal>);
  }
}

export default AddListing;
