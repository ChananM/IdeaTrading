import React from 'react';

class Board extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            orders: []
        }
    }

    componentDidMount() {
        setInterval(() => {
            fetch('http://192.168.1.117:8080/IdeaTrading/rest/orders')
                .then(res => res.json())
                .then(
                    (result) => {
                        let orders = [];
                        for (let key of Object.keys(result)) {
                            orders.push(result[key]);
                        }
                        this.setState({
                            orders: orders
                        })
                    }
                )
        }, 1000);
    }

    render() {
        return (
            <div className="board">
                {this.state.orders.map((value) => {
                    return (<div>
                        <span>{value.type} - {value.price}</span>
                    </div>)
                })}
            </div>
        );
    }
}
export default Board;