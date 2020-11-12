import React from 'react';
import Idea from './idea';

class Board extends React.Component {

    intervalId;

    constructor(props) {
        super(props);
        this.state = {
            ordersList: []
        }
    }

    componentDidMount() {
        this.intervalId = setInterval(() => this.getAllOrders(), 1000);
    }

    componentWillUnmount() {
        clearInterval(this.intervalId);
    }

    getAllOrders() {
        fetch('http://localhost:8080/IdeaTrading/rest/orders')
            .then(res => res.json())
            .then((result) => this.updateBoard(result));
    }

    updateBoard(orders) {
        let list = [];
        for (let [key, value] of Object.entries(orders)) {
            list.push(
                <Idea key={key} name={key} orders={value} />
            )
        }
        this.setState({
            ordersList: list
        })
    }

    render() {
        return (
            <div className="board">
                {this.state.ordersList}
            </div>
        );
    }
}
export default Board;