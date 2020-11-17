import React from 'react';
import Idea from './idea';

class Board extends React.Component {

    updateBoard(orders) {
        let list = [];
        if (orders) {
            for (let [key, value] of Object.entries(orders)) {
                list.push(
                    <Idea key={key} name={key} orders={value} username={this.props.username} password={this.props.password} />
                )
            }
        }
        return list;
    }

    render() {
        return (
            <div className="board">
                {this.updateBoard(this.props.ordersList)}
            </div>
        );
    }
}
export default Board;