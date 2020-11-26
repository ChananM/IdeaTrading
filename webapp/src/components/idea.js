import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, ThemeProvider } from '@material-ui/core';
import React from 'react';
import { getTheme } from '../util/util';
import IdeaOrderListing from './ideaOrderListing';
import OrderButton from './orderButton';

class Idea extends React.Component {

    render() {
        return (
            <div className="idea">
                <ThemeProvider theme={getTheme()}>
                    <TableContainer component={Paper}>
                        <Table aria-label="simple table">
                            <TableHead >
                                <TableRow >
                                    <TableCell className="centered">{this.props.name}</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                <TableRow>
                                    <TableCell component="th" scope="row">
                                        <IdeaOrderListing orders={this.props.orders} />
                                    </TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell className="centered" component="th" scope="row">
                                        <OrderButton idea={this.props.name} username={this.props.username} password={this.props.password} />
                                    </TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>
                </ThemeProvider>
            </div>
        );
    }
}
export default Idea;