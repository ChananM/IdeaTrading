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
                            <OrderButton idea={this.props.name} />
                        </td>
                    </tr>
                </tbody>
            </table>
        );
    }
}
export default Idea;