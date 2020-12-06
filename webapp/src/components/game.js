import React from 'react';
import Board from './board';
import Login from './login';
import PlayerArea from './playerArea';

const axios = require('axios').default;

export default class Game extends React.Component {

    state = {
        username: undefined,
        password: undefined,
        loggedIn: false,
        gameData: { orders: {}, playerData: {} }
    };

    login(username, password) {
        axios.get('/IdeaTrading/rest/game/login?playerName=' + username + '&password=' + password)
            .then((response) => {
                let loggedIn = response.data;
                if (loggedIn) {
                    this.openDataSocket(username);
                    this.setState({
                        username: username,
                        password: password,
                        loggedIn: loggedIn,
                    });
                } else {
                    alert('Login failed.\nWrong username or password');
                }
            });
    }

    openDataSocket(username) {
        this.ideasSocket = new WebSocket("ws://" + window.location.hostname + "/IdeaTrading/ideasSocket/" + username);
        this.ideasSocket.onmessage = (event) => {
            this.setState({
                gameData: JSON.parse(event.data)
            });
        }
        // ideasSocket.onopen = (event) => { };
        this.ideasSocket.onclose = (event) => {
            alert("Connection to the server lost.\nPlease refresh the page and login again.");
        };
    }

    componentWillUnmount() {
        this.ideasSocket.close();
    }

    render() {
        return (
            <div>
                { this.state.loggedIn ?
                    <div className="flex flex-row">
                        <PlayerArea username={this.state.username} password={this.state.password} playerData={this.state.gameData.playerData} />
                        <Board username={this.state.username} password={this.state.password} ordersList={this.state.gameData.orders} />
                    </div>
                    :
                    <Login onLogin={(username, password) => this.login(username, password)} />}
            </div>
        );
    }
}
