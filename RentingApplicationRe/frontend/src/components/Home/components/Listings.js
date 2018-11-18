import React, {Component} from 'react';
import 'bulma';
import {
  Container,
  Column,
  Columns
} from 'bloomer';
import ListingEntity from './ListingEntity';

class Listings extends Component {
  constructor(props) {
    super(props);
    this.generateElements = this.generateElements.bind(this);
  }
  componentDidUpdate() {
    window.scrollTo(0, 0);
  }
  generateElements() {
    if (this.props.listings.length === 0) {
      return <p>Is seems you've reached the end</p>;
    }
    if (this.props.listings.length <= 3) {
      return (<Columns isCentered="isCentered">
        {
          this.props.listings.slice(0, 3).map(listing => (<Column isSize="1/4">
            <ListingEntity enableEdit={this.props.enableEdit} uid={this.props.uid} lid={listing.id} luid={listing.uid} title={listing.title} image={listing.imagePath} description={listing.description} locality={listing.locality} trigger={this.props.trigger} interested={listing.interested} contact={this.props.contact}/>
          </Column>))
        }
      </Columns>);
    } else {
      return (<><Columns isCentered="isCentered">
        {
          this.props.listings.slice(0, 3).map(listing => (<Column isSize="1/4">
            <ListingEntity enableEdit={this.props.enableEdit} uid={this.props.uid} lid={listing.id} luid={listing.uid} title={listing.title} image={listing.imagePath} description={listing.description} locality={listing.locality} trigger={this.props.trigger} interested={listing.interested} contact={this.props.contact}/>
          </Column>))
        }
      </Columns>
      <Columns isCentered="isCentered">
        {
          this.props.listings.slice(3).map(listing => (<Column isSize="1/4">
            <ListingEntity enableEdit={this.props.enableEdit} uid={this.props.uid} lid={listing.id} luid={listing.uid} title={listing.title} image={listing.imagePath} description={listing.description} locality={listing.locality} trigger={this.props.trigger} interested={listing.interested} contact={this.props.contact}/>
          </Column>))
        }
      </Columns></>);
    }
  }
  render() {
    return (<Container>
      {this.generateElements()}
    </Container>);
  }
}

export default Listings;
