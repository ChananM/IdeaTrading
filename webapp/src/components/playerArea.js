import React from 'react';

export default class PlayerArea extends React.Component {

    render() {
        return (
            <div className="playerArea white">
                <div className="margin-top flex flex-row">
                    <span className="centeredSpan">{this.props.username}</span>
                    <span className="centeredSpan">Money: {this.props.playerData.money}</span>
                </div>
                {/* TODO: Add table for open orders with cancel button and table for current stock holdings */}
            </div>
        );
    }
}