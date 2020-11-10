
import React from 'react';
import logo from './logo.svg';
import './App.css';

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      orders: []
    }
  }

  componentDidMount() {
    setInterval(() => {
      fetch('http://localhost:8080/IdeaTrading/rest/orders')
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
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          {this.state.orders.map((value) => {
            return <div>
              <span>{value.type}</span>
              <span>{value.price}</span>
            </div>
          })}
        </header>
      </div>)
  }
}

export default App;
