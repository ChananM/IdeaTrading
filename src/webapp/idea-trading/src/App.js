
import React from 'react';
import logo from './logo.svg';
import './App.css';

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      number: 0
    }
  }

  componentDidMount() {
    document.addEventListener('keydown', () => {
      fetch('http://localhost:8080/IdeaTrading/rest/pub/2')
        .then(res => res.json())
        .then(
          (result) => {
            this.setState({
              number: result.number
            })
          }
        )
    });
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            {this.state.number}
          </p>
          <a
            className="App-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React today
      </a>
        </header>
      </div>)
  }
}

export default App;
