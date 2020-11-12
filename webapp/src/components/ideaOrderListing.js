import React from 'react';

class IdeaOrderListing extends React.Component {

    getListing() {
        let listing = [];
        for (let [key, value] of Object.entries(this.props.orders)) {
            listing.push(
                <tr key={key}>
                    <td>{value.type} - {value.price}</td>
                    <td>{value.type} - {value.price}</td>
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