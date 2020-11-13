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

const axios = require('axios').default

class OrderButton extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: false,
            orderType: 'BUY',
            orderAmount: 0
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
            open: false
        });
    };

    sendOrder() {
        axios.post('http://localhost:8080/IdeaTrading/rest/' + this.props.idea, {
            ownerName: 'chanan',
            orderType: this.state.orderType,
            price: this.state.orderAmount
        }).then((response) => alert(response.data));
    }

    render() {
        return (
            <div>
                <Button variant="outlined" color="inherit" onClick={this.handleClickOpen}>Put Order</Button>
                <Dialog open={this.state.open} onClose={this.handleClose} aria-labelledby="form-dialog-title">
                    <DialogTitle id="form-dialog-title">Put order on {this.props.idea}</DialogTitle>
                    <DialogContent>
                        <DialogContentText>Select order type and amount</DialogContentText>
                        <RadioGroup row aria-label="orderType" name="orderType" value={this.state.orderType} onChange={(event) => this.setState({ orderType: event.target.value })}>
                            <FormControlLabel value="BUY" control={<Radio />} label="Buy" />
                            <FormControlLabel value="SELL" control={<Radio />} label="Sell" />
                        </RadioGroup>
                        <TextField autoFocus margin="dense" id="amount" label="Amount" type="number" fullWidth onChange={(event) => this.setState({ orderAmount: event.target.value })} />
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color="primary">Cancel</Button>
                        <Button onClick={this.sendOrder} color="primary">Send</Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}
export default OrderButton;