import React, {Component} from 'react';
import 'bulma';
import {Pagination, PageControl} from 'bloomer';

class Bottom extends Component {
  render() {
    return (<Pagination>
      <PageControl onClick={this.props.prev}>Previous</PageControl>
      <PageControl onClick={this.props.next}>Next</PageControl>
    </Pagination>);
  }
}

export default Bottom;
