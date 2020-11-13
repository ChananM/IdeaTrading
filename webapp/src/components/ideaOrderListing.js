import React from 'react';

class IdeaOrderListing extends React.Component {

    getListing() {
        let listing = [];
        let buyOrders = Object.entries(this.props.orders.BUY);
        let sellOrders = Object.entries(this.props.orders.SELL);
        for (let i = 0; i < Math.max(buyOrders.length, sellOrders.length); i++) {
            listing.push(
                <tr key={(buyOrders[i] ? buyOrders[i][0] : 'N') + ' - ' + (sellOrders[i] ? sellOrders[i][0] : 'N')}>
                    {buyOrders[i] ? <td>{buyOrders[i][1].price}</td> : <td> - </td>}
                    {sellOrders[i] ? <td>{sellOrders[i][1].price}</td> : <td> - </td>}
                </tr>
            )
        }
        return listing;
    }

    render() {
        return (
            <table>
                <tbody>
                    <tr>
                        <th>Buy Orders</th>
                        <th>Sell Orders</th>
                    </tr>
                    {this.getListing()}
                </tbody>
            </table>
        );
    }
}
export default IdeaOrderListing;