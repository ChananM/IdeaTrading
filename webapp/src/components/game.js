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
        axios.get('http://localhost:8080/IdeaTrading/rest/game/login?playerName=' + username + '&password=' + password)
            .then((response) => {
                let loggedIn = response.data;
                this.intervalId = setInterval(() => this.getGameData(), 1000);
                if (loggedIn) {
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

    getGameData() {
        axios.get('http://localhost:8080/IdeaTrading/rest/game/data?playerName=' + this.state.username + '&password=' + this.state.password)
            .then((response) => {
                this.setState({
                    gameData: response.data
                });
            });
    }

    componentWillUnmount() {
        clearInterval(this.intervalId);
    }

    render() {
        return (
            <div className="App">
                { this.state.loggedIn ?
                    <div className="flex flex-row">
                        <PlayerArea username={this.state.username} playerData={this.state.gameData.playerData} />
                        <Board username={this.state.username} password={this.state.password} ordersList={this.state.gameData.orders} />
                    </div>
                    :
                    <Login onLogin={(username, password) => this.login(username, password)} />}
            </div>
        );
    }
}
