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
      userInfo: {},
      lusername : undefined,
      ltitle : undefined,
      searchLocality : undefined
    };
    this.getListings = this.getListings.bind(this);
    this.setFrame = this.setFrame.bind(this);
    this.nextFrame = this.nextFrame.bind(this);
    this.prevFrame = this.prevFrame.bind(this);
    this.showMy = this.showMy.bind(this);
    this.showInterested = this.showInterested.bind(this);
    this.getUserInfo = this.getUserInfo.bind(this);
    this.contact = this.contact.bind(this);
    this.unset = this.unset.bind(this);
    this.search = this.search.bind(this);
    this.getUserInfo();
    this.getListings();
    this.setFrame();
  }
  search(locality) {
    let prom = new Promise((res, rej) => {
      this.setState({"searchLocality" : locality});
      res();
    });
    prom.then(() => {
      this.getListings();
    });
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
      if (this.state.searchLocality !== undefined)
        params.append("locality", this.state.searchLocality);
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
        let query = "Listing";
        if (this.state.searchLocality !== undefined)
          query += "?locality="+this.state.searchLocality;
        axios.get(query).then(res => {
          this.setState({
            listings: JSON.parse(res.data.listings)
          });
          this.setFrame();
        });
      } else {
        const fd = new URLSearchParams();
        fd.append("showMy", "true");
        if (this.state.searchLocality !== undefined)
          fd.append("locality", this.state.searchLocality);
        axios.get("Listing?" + fd.toString()).then(res => {
          this.setState({
            listings: JSON.parse(res.data.listings)
          });
          this.setFrame();
        });
      }
    }
  }
  contact(luid, title) {
    const params = new URLSearchParams();
    params.append("luid", luid);
    axios.get("User?"+params.toString()).then(res => {
      if (res.data.username !== undefined || res.data.username !== null) {
        this.setState({lusername : res.data.username, ltitle : title});
      }
    });
  }

  unset() {
    this.setState({lusername : undefined, ltitle : undefined});
  }

  render() {
    return (<div>
      <Top logout={this.props.logout} showMy={this.showMy} showInterested={this.showInterested} username={this.state.userInfo.username}/>
      <Section>
        <Middle listings={this.state.currentFrame} trigger={this.getListings} uid={this.props.uid} contact={this.contact} search={this.search}/>
      </Section>
      <Section>
        <Bottom next={this.nextFrame} prev={this.prevFrame}/>
      </Section>
      <Message userInfo={this.state.userInfo} message={{lusername : this.state.lusername, ltitle : this.state.ltitle}} unset={this.unset}/>
    </div>);
  }
}

export default Home;
