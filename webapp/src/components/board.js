import React from 'react';
import Idea from './idea';

const axios = require('axios').default;

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
        axios.get('http://localhost:8080/IdeaTrading/rest/orders')
            .then((response) => this.updateBoard(response.data));
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