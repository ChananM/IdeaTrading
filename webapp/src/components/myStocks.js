import React from 'react';
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, ThemeProvider } from '@material-ui/core';
import { getTheme } from '../util/util';

export default class MyStocks extends React.Component {

    render() {
        return (
            <div className="playerAreaModule">
                <ThemeProvider theme={getTheme()}>
                    <TableContainer component={Paper}>
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Idea</TableCell>
                                    <TableCell align="right">Amount</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {Object.entries(this.props.stocks).map(row => (
                                    <TableRow key={row[0]}>
                                        <TableCell component="th" scope="row">{row[0]}</TableCell>
                                        <TableCell align="right">{row[1]}</TableCell>
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