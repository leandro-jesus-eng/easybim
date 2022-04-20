import React, { Component } from 'react';
import { TieredMenu } from 'primereact/tieredmenu';
import { Button } from 'primereact/button';

export class MenuOptions extends Component {

    constructor() {
       super();
    }

    render() {
        return (
            <div>                
                <TieredMenu model={this.props.items} popup ref={el => this.menu = el} id="overlay_tmenu" style={{ width: '200px' }} />
                <Button className= "p-button-outlined p-button-text p-button-icon-only" label="" icon="pi pi-book" onClick={(event) => this.menu.toggle(event)} aria-haspopup aria-controls="overlay_tmenu"/>                
            </div>
        );
    }
}