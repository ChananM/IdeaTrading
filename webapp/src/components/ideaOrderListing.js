import React from 'react';

class IdeaOrderListing extends React.Component {

    getListing() {
        let listing = [];
        let buyOrders = Object.entries(this.props.orders.BUY).sort((a, b) => { return b[1].pricePerShare - a[1].pricePerShare });
        let sellOrders = Object.entries(this.props.orders.SELL).sort((a, b) => { return a[1].pricePerShare - b[1].pricePerShare });
        for (let i = 0; i < Math.max(buyOrders.length, sellOrders.length); i++) {
            listing.push(
                <tr key={(buyOrders[i] ? buyOrders[i][0] : 'N') + ' - ' + (sellOrders[i] ? sellOrders[i][0] : 'N')}>
                    <td >
                        <div className="flex flex-row">
                            {buyOrders[i] ? <span className="centeredSpan">{buyOrders[i][1].shareAmount}</span> : <span className="centeredSpan"> - </span>}
                            {buyOrders[i] ? <span className="centeredSpan green">{buyOrders[i][1].pricePerShare}</span> : <span className="centeredSpan"> - </span>}
                        </div>
                    </td>
                    <td >
                        <div className="flex flex-row">
                            {sellOrders[i] ? <span className="centeredSpan red">{sellOrders[i][1].pricePerShare}</span> : <span className="centeredSpan"> - </span>}
                            {sellOrders[i] ? <span className="centeredSpan">{sellOrders[i][1].shareAmount}</span> : <span className="centeredSpan"> - </span>}
                        </div>
                    </td>
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