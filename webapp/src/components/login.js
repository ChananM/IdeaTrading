import React from 'react';
import { TextField, Button } from '@material-ui/core';

export default class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        }
        this.handleLogin = this.handleLogin.bind(this);
    }

    handleLogin() {
        this.props.onLogin(this.state.username, this.state.password);
    }

    render() {
        return (
            <form className='loginForm'>
                <TextField
                    autoFocus
                    margin="dense"
                    id="username"
                    label="Username"
                    type="text"
                    onChange={(event) => this.setState({ username: event.target.value })} />
                <TextField
                    margin="dense"
                    id="password"
                    label="Password"
                    type="password"
                    onChange={(event) => this.setState({ password: event.target.value })} />
                <Button onClick={this.handleLogin} color="primary">Login</Button>
            </form>
        );
    }
}