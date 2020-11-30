import React from 'react';
import { Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, ThemeProvider } from '@material-ui/core';
import ClearIcon from '@material-ui/icons/Clear';
import { getTheme } from '../util/util';

const axios = require('axios').default;

export default class MyOpenOrders extends React.Component {

    cancelOrder(idea, orderType, orderId) {
        axios.delete('/IdeaTrading/rest/orders/' + idea + '/' + orderType + '/' + orderId + '?playerName=' + this.props.username + '&password=' + this.props.password)
            .then((response) => {
                // alert('Order canceled');
            })
    }

    render() {
        return (
            <div className="playerAreaModule">
                <ThemeProvider theme={getTheme()}>
                    <TableContainer component={Paper}>
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Idea</TableCell>
                                    <TableCell align="right">Type</TableCell>
                                    <TableCell align="right">Shares</TableCell>
                                    <TableCell align="right">Price</TableCell>
                                    <TableCell align="right" />
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {Object.values(this.props.openOrders).map(row => (
                                    <TableRow key={row.id}>
                                        <TableCell component="th" scope="row">{row.idea}</TableCell>
                                        <TableCell align="right">{row.type}</TableCell>
                                        <TableCell align="right">{row.shareAmount}</TableCell>
                                        <TableCell align="right">{row.pricePerShare}</TableCell>
                                        <TableCell align="right">
                                            <Button onClick={() => this.cancelOrder(row.idea, row.type, row.id)}>
                                                <ClearIcon className="red" aria-label="edit">X</ClearIcon>
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </ThemeProvider>
            </div>

        );
    }
}