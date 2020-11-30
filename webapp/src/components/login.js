import React from 'react';
import { TextField, Button, ThemeProvider } from '@material-ui/core';
import { getTheme } from '../util/util';

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
            <form className='loginForm white'>
                <ThemeProvider theme={getTheme()}>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="username"
                        label="Idea Name"
                        type="text"
                        onChange={(event) => this.setState({ username: event.target.value })} />
                    <TextField
                        margin="dense"
                        id="password"
                        label="Password"
                        type="password"
                        onChange={(event) => this.setState({ password: event.target.value })} />
                    <Button onClick={this.handleLogin}>Login</Button>
                </ThemeProvider>
            </form>
        );
    }
}