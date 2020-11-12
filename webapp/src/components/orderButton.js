import React from 'react';

class OrderButton extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            type: props.type
        };
    }

    render() {
        return (
            <button>{this.state.type}</button>
        );
    }
}
export default OrderButton;