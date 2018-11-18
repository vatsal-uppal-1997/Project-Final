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

class EditListing extends Component {

  constructor(props) {
    super(props);
    this.state = {
      title: this.props.default.title,
      locality: this.props.default.locality,
      description: this.props.default.description,
      image: null
    }
    this.handleEvents = this.handleEvents.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.reset = this.reset.bind(this);
  }
  reset() {
    this.setState({title: this.props.default.title, locality: this.props.default.locality, description: this.props.default.description, image: null});
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
    fd.append("id", this.props.default.id);
    for (const key of Object.keys(this.state)) {
      if (key !== "image" && this.state[[key]] !== this.props.default[[key]])
        fd.append([key], this.state[[key]].trim());
    }
    if (this.state.image !== null)
      fd.append("image", this.state.image, this.state.image.name);
    axios.post("Listing", fd).then(res => {
      if (res.data.message === "Listing Successfully Updated") {
          this.props.disable();
      }
    });
  }
  render() {
    return (<Modal isActive={this.props.isActive}>
      <ModalBackground/>
      <ModalCard>
        <ModalCardHeader>
          <ModalCardTitle>Edit listing</ModalCardTitle>
          <Delete onClick={this.props.disable}/>
        </ModalCardHeader>
        <ModalCardBody>
          <form onSubmit={this.handleSubmit}>
            <Field>
              <Label>Edit Title</Label>
              <Control>
                <Input type="text" onChange={e => this.handleEvents(e, "title")} value={this.state.title || ""}/>
              </Control>
            </Field>
            <Field>
              <Label>Edit Locality</Label>
              <Control>
                <Input type="text" onChange={e => this.handleEvents(e, "locality")} value={this.state.locality || ""}/>
              </Control>
            </Field>
            <Field>
              <Label>Edit Description</Label>
              <Control>
                <TextArea onChange={e => this.handleEvents(e, "description")} value={this.state.description || ""}/>
              </Control>
            </Field>
            <Field>
              <Label>Upload new image</Label>
              <Control>
                <Input type="file" accept="image/*" onChange={e => this.handleEvents(e, "image")}/>
              </Control>
            </Field>
            <Field isGrouped>
              <Control>
                <Button type="submit" isColor='success'>Update</Button>
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

export default EditListing;
