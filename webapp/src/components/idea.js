import React from 'react';
import IdeaOrderListing from './ideaOrderListing';
import OrderButton from './orderButton';

class Idea extends React.Component {

    render() {
        return (
            <table>
                <tbody>
                    <tr>
                        <th>{this.props.name}</th>
                    </tr>
                    <tr>
                        <td><IdeaOrderListing orders={this.props.orders} /></td>
                    </tr>
                    <tr>
                        <td>
                            <OrderButton type="BUY" />
                            <OrderButton type="SELL" />
                        </td>
                    </tr>
                </tbody>
            </table>
        );
    }
}
export default Idea;