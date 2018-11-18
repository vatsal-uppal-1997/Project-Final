import React, {Component} from 'react';
import 'bulma';
import axios from 'axios';
import {Section} from 'bloomer';
import Top from './components/Top';
import Middle from './components/Middle';
import Bottom from './components/Bottom';
import Message from './components/Message';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      listings: [],
      currentFrameIndex: 0,
      showMy: false,
      showInterested : false,
      currentFrame: [],
      userInfo: {}
    };
    this.getListings = this.getListings.bind(this);
    this.setFrame = this.setFrame.bind(this);
    this.nextFrame = this.nextFrame.bind(this);
    this.prevFrame = this.prevFrame.bind(this);
    this.showMy = this.showMy.bind(this);
    this.showInterested = this.showInterested.bind(this);
    this.getUserInfo = this.getUserInfo.bind(this);
    this.getUserInfo();
    this.getListings();
    this.setFrame();
  }
  getUserInfo() {
    axios.get("User").then(res => {
      if ("message" in res.data)
        this.props.logout();
      this.setState({userInfo: res.data});
    });
  }
  showInterested() {
    let stateChange = new Promise((resolve, reject)=>{
      this.setState(currentState => ({
        showInterested: !currentState.showInterested,
        showMy : false
      }));
        resolve();
    });
    console.log("called");
    stateChange.then((value) => {
      this.getListings();
      this.setFrame();
    });
  }
  showMy() {
    let stateChange = new Promise((resolve, reject)=>{
      this.setState(currentState => ({
        showMy: !currentState.showMy,
        showInterested : false
      }));
        resolve();
    });
    stateChange.then((value) => {
      this.getListings();
      this.setFrame();
    });
  }
  setFrame() {
    let Frame = this.state.listings.slice(this.state.currentFrameIndex, this.state.currentFrameIndex + 6);
    this.setState({currentFrame: Frame});
  }
  nextFrame() {
    this.getListings();
    if ((this.state.currentFrameIndex+6) >= this.state.listings.length)
      return;
    this.setState(currentState => ({
      currentFrameIndex: currentState.currentFrameIndex + 6
    }));
  }
  prevFrame() {
    this.getListings();
    if ((this.state.currentFrameIndex-6) < 0)
      return;
    this.setState(currentState => ({
      currentFrameIndex: currentState.currentFrameIndex - 6
    }));
  }
  getListings() {
    if (this.state.showInterested === true) {
      const params = new URLSearchParams();
      params.append("uid", this.props.uid);
      params.append("showInterested", "true");
      axios.get("Listing?"+params.toString()).then(res => {
        let listings = JSON.parse(res.data.listings);
        if (listings !== [])
          listings = listings.map(listing => {
            listing.interested = true;
            return listing;
          });
        this.setState({
          listings: listings
        });
        this.setFrame();
      });
    } else {
      if (this.state.showMy === false) {
        axios.get("Listing").then(res => {
          this.setState({
            listings: JSON.parse(res.data.listings)
          });
          this.setFrame();
        });
      } else {
        const fd = new URLSearchParams();
        fd.append("showMy", "true");
        axios.get("Listing?" + fd.toString()).then(res => {
          this.setState({
            listings: JSON.parse(res.data.listings)
          });
          this.setFrame();
        });
      }
    }
  }
  render() {
    return (<div>
      <Top logout={this.props.logout} showMy={this.showMy} showInterested={this.showInterested} username={this.state.userInfo.username}/>
      <Section>
        <Middle listings={this.state.currentFrame} trigger={this.getListings} uid={this.props.uid}/>
      </Section>
      <Section>
        <Bottom next={this.nextFrame} prev={this.prevFrame}/>
      </Section>
      <Message userInfo={this.state.userInfo}/>
    </div>);
  }
}

export default Home;
