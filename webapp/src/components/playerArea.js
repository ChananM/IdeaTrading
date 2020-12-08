import React from 'react';
import MyOpenOrders from './myOpenOrders';
import MyStocks from './myStocks';

import Notifications from './notifications';
import { Divider } from '@material-ui/core';

export default class PlayerArea extends React.Component {

    render() {
        return (
            <div className="playerArea flex flex-column white">
                <div className="margin-top flex flex-row">
                    <Notifications username={this.props.username}></Notifications>
                    <span className="centeredSpan align-self">{this.props.username}</span>
                    <span className="centeredSpan align-self">Money: {this.props.playerData.money}</span>
                </div>
                <div className="margin-height">
                    <Divider />
                </div>
                <span>My open orders:</span>
                {this.props.playerData.openOrders ? <MyOpenOrders username={this.props.username} password={this.props.password} openOrders={this.props.playerData.openOrders} /> : <span>No open orders</span>}
                <span>My stocks:</span>
                {this.props.playerData.stocks ?
                    <MyStocks stocks={this.props.playerData.stocks} /> :
                    <span>No owned stocks</span>}
            </div>
        );
    }
}