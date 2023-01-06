import React, { Component } from 'react';
import PropTypes from 'prop-types';

import "../styles/ToggleSwitch.css"

export class ToggleSwitch extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {
        const {selected,toggleSelected} = this.props;
        return (
            <div className="toggle-container" onClick={toggleSelected}>
                <div className={`dialog-button ${selected ? "" : "dark"}`}>
                    {selected ? "LIGHT" : "DARK"}
                </div>
            </div>
        )
    }
}

ToggleSwitch.propTypes = {
    selected: PropTypes.bool.isRequired,
    toggleSelected: PropTypes.func.isRequired
};



