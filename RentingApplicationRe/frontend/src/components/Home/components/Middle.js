import React, {Component} from 'react';
import 'bulma';
import {
  Container,
  Level,
  LevelLeft,
  LevelRight,
  LevelItem,
  Field,
  Control,
  Input,
  Button,
  Section
} from 'bloomer';
import Listings from './Listings';
import AddListing from './AddListing';
import EditListing from './EditListing';

class Middle extends Component {
  constructor(props) {
    super(props);
    this.state = {modal : false, edit: false, defaultValues : {}};
    this.enableEdit = this.enableEdit.bind(this);
    this.disable = this.disable.bind(this);
    this.disableEdit = this.disableEdit.bind(this);
    this.enable = this.enable.bind(this);
  }
  enableEdit(defaultValues) {
    this.state.defaultValues = defaultValues;
    this.setState({edit : true});
  }
  disable() {
    this.props.trigger();
    this.setState({modal : false});
  }
  disableEdit() {
    this.props.trigger();
    this.setState({edit : false});
  }
  enable() {
    this.setState({modal : true});
  }
  render() {
    return (<Container>
      <Section>
        <Level>
          <LevelLeft>
            <LevelItem>
              <Field hasAddons>
                <Control>
                  <Input type="text" placeholder="Search by Locality"/>
                </Control>
                <Control>
                  <Button>Search</Button>
                </Control>
              </Field>
            </LevelItem>
          </LevelLeft>
          <LevelRight>
            <LevelItem>
              <Field>
                <Control>
                  <Button isColor="success" onClick={this.enable}>Add a Listing</Button>
                </Control>
              </Field>
            </LevelItem>
          </LevelRight>
        </Level>
        <hr></hr>
      </Section>
      <Section>
        <AddListing isActive={this.state.modal} disable={this.disable}/>
        {this.state.edit && <EditListing isActive={this.state.edit} disable={this.disableEdit} default={this.state.defaultValues}/>}
        <Listings listings={this.props.listings} uid={this.props.uid} enableEdit={this.enableEdit} trigger={this.props.trigger}/>
      </Section>
    </Container>);
  }
}

export default Middle;
