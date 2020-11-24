import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Radio from '@material-ui/core/Radio'
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { createMuiTheme, ThemeProvider } from '@material-ui/core';

const axios = require('axios').default

const theme = createMuiTheme({
    palette: {
        type: "dark"
    }
});

export default class OrderButton extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: false,
            orderType: 'BUY',
            orderPrice: 0,
            orderAmount: 0,
            confirmationOpen: false,
            orderResponse: undefined,
        };

        this.handleClickOpen = this.handleClickOpen.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.sendOrder = this.sendOrder.bind(this);
    }

    handleClickOpen() {
        this.setState({
            open: true
        });
    };

    handleClose() {
        this.setState({
            open: false,
            confirmationOpen: false
        });
    };

    sendOrder() {
        axios.post('/IdeaTrading/rest/orders/' + this.props.idea, {
            playerName: this.props.username,
            password: this.props.password,
            idea: this.props.idea,
            ownerName: this.props.username,
            orderType: this.state.orderType,
            shareAmount: this.state.orderAmount,
            pricePerShare: this.state.orderPrice
        }).then((response) => {
            this.setState({
                orderPrice: 0,
                orderAmount: 0,
                open: false,
                confirmationOpen: true,
                orderResponse: response.data
            });
        });
    }

    render() {
        return (
            <div>
                <Button variant="outlined" color="inherit" onClick={this.handleClickOpen}>Put Order</Button>
                <ThemeProvider theme={theme}>
                    <Dialog open={this.state.open} onClose={this.handleClose} aria-labelledby="form-dialog-title">
                        <DialogTitle id="form-dialog-title">Put order on {this.props.idea}</DialogTitle>
                        <DialogContent>
                            <DialogContentText>Select order type and amount</DialogContentText>
                            <DialogContentText>Please note the price is per share and not the total price</DialogContentText>
                            <RadioGroup row aria-label="orderType" name="orderType" value={this.state.orderType} onChange={(event) => this.setState({ orderType: event.target.value })}>
                                <FormControlLabel value="BUY" control={<Radio />} label="Buy" />
                                <FormControlLabel value="SELL" control={<Radio />} label="Sell" />
                            </RadioGroup>
                            <TextField error={this.state.orderAmount <= 0 || !/^([0-9]\d*)$/.test(this.state.orderAmount)}
                                helperText="Value must be an integer bigger than 0"
                                autoFocus
                                margin="dense"
                                id="amount"
                                label="How much shares?"
                                type="number"
                                fullWidth
                                color="secondary"
                                onChange={(event) => this.setState({ orderAmount: event.target.value })} />
                            <TextField error={this.state.orderPrice <= 0}
                                helperText="Value must be a number bigger than 0"
                                margin="dense"
                                id="price"
                                label="For how much per share?"
                                type="number"
                                color="secondary"
                                fullWidth onChange={(event) => this.setState({ orderPrice: event.target.value })} />
                        </DialogContent>
                        <DialogActions>
                            <Button color="inherit" onClick={this.handleClose}>Cancel</Button>
                            <Button disabled={this.state.orderAmount <= 0 || !/^([0-9]\d*)$/.test(this.state.orderAmount) || this.state.orderPrice <= 0}
                                onClick={this.sendOrder}
                                color="inherit">
                                Send
                        </Button>
                        </DialogActions>
                    </Dialog>
                    <Dialog
                        open={this.state.confirmationOpen}
                        onClose={this.handleClose}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description">
                        <DialogTitle id="alert-dialog-title">Order confirmation</DialogTitle>
                        <DialogContent>
                            <DialogContentText id="alert-dialog-description">
                                {this.state.orderResponse}
                            </DialogContentText>
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={this.handleClose} color="inherit" autoFocus>
                                Ok
                        </Button>
                        </DialogActions>
                    </Dialog>
                </ThemeProvider>
            </div>
        );
    }
}