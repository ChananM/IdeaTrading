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
                            {buyOrders[i] ? <span className="centeredSpan big-font">{buyOrders[i][1].shareAmount}</span> : <span className="centeredSpan big-font"> - </span>}
                            {buyOrders[i] ? <span className="centeredSpan green big-font">{buyOrders[i][1].pricePerShare}</span> : <span className="centeredSpan big-font"> - </span>}
                        </div>
                    </td>
                    <td >
                        <div className="flex flex-row">
                            {sellOrders[i] ? <span className="centeredSpan red big-font">{sellOrders[i][1].pricePerShare}</span> : <span className="centeredSpan big-font"> - </span>}
                            {sellOrders[i] ? <span className="centeredSpan big-font">{sellOrders[i][1].shareAmount}</span> : <span className="centeredSpan big-font"> - </span>}
                        </div>
                    </td>
                </tr>
            )
        }
        return listing;
    }

    render() {
        return (
            <table className="mid-width">
                <tbody>
                    <tr >
                        <th className="centered">Buy</th>
                        <th className="centered">Sell</th>
                    </tr>
                    <tr>
                        <td className="bottom-border" />
                        <td className="bottom-border" />
                    </tr>
                    <tr>
                        <th className="small-width centered">
                            <span className="margin-width">shares</span>
                            <span className="margin-width">price</span>
                        </th>
                        <th className="small-width centered">
                            <span className="margin-width">price</span>
                            <span className="margin-width">shares</span>
                        </th>
                    </tr>
                    {this.getListing()}
                </tbody>
            </table>
        );
    }
}
export default IdeaOrderListing;