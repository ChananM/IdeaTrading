import { Button, List, ListItem, ListItemIcon, ListItemText, Paper, ThemeProvider } from '@material-ui/core';
import NotificationsIcon from '@material-ui/icons/Notifications';
import NotificationsActiveIcon from '@material-ui/icons/NotificationsActive';
import React from 'react';
import { getTheme } from '../util/util';
import FiberManualRecordIcon from '@material-ui/icons/FiberManualRecord';

export default class Notifications extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            hideNotifications: true,
            newNotifications: false,
            readNotifications: 0,
            notifications: []
        }
        this.setWrapperRef = this.setWrapperRef.bind(this);
        this.handleClickOutside = this.handleClickOutside.bind(this);
    }

    componentDidMount() {
        this.notificationsSocket = new WebSocket("ws://" + window.location.hostname + "/IdeaTrading/notifications/" + this.props.username);
        this.notificationsSocket.onmessage = (event) => {
            let notifications = this.state.notifications;
            notifications.push(event.data);
            this.setState({
                notifications: notifications,
                newNotifications: true
            });
            new Audio('https://proxy.notificationsounds.com/message-tones/juntos-607/download/file-sounds-1148-juntos.mp3').play();
        }
        this.notificationsSocket.onclose = (event) => {
            alert("Notifications connection lost.\nPlease refresh the page and login again." + event.data);
        };
        document.addEventListener('mousedown', this.handleClickOutside);
    }

    componentWillUnmount() {
        this.notificationsSocket.close();
        document.removeEventListener('mousedown', this.handleClickOutside);
    }

    setWrapperRef(node) {
        this.wrapperRef = node;
    }

    handleClickOutside(event) {
        if (this.wrapperRef && !this.wrapperRef.contains(event.target)) {
            this.setState({
                hideNotifications: true
            });
        }
    }

    toggleNotifications() {
        this.setState({
            readNotifications: (this.state.hideNotifications ? this.state.notifications.length : this.state.readNotifications),
            hideNotifications: !this.state.hideNotifications,
            newNotifications: false
        });
    }

    render() {
        return (
            <div ref={this.setWrapperRef} className="centeredSpan align-self">
                <ThemeProvider theme={getTheme()}>
                    <Button onClick={() => this.toggleNotifications()}>
                        {this.state.newNotifications ?
                            <div>
                                <NotificationsActiveIcon></NotificationsActiveIcon>
                                <div className="notification-indicator">{this.state.notifications.length - this.state.readNotifications}</div>
                            </div>
                            : <NotificationsIcon></NotificationsIcon>}
                    </Button>
                    <div className={"notification-panel " + (this.state.hideNotifications ? 'hide' : '')} >
                        <Paper elevation={6}>
                            <List component="nav" aria-label="main mailbox folders">
                                {this.state.notifications.length === 0 ?
                                    <ListItem key="0" button>
                                        <ListItemIcon>
                                            <FiberManualRecordIcon fontSize="inherit" />
                                        </ListItemIcon>
                                        <ListItemText primary="There are no notifications" />
                                    </ListItem>
                                    :
                                    this.state.notifications.map((notification) => {
                                        return (
                                            <ListItem key={notification} button>
                                                <ListItemIcon>
                                                    <FiberManualRecordIcon fontSize="inherit" />
                                                </ListItemIcon>
                                                <ListItemText primary={notification} />
                                            </ListItem>
                                        )
                                    })
                                }
                            </List>
                        </Paper>
                    </div>
                </ThemeProvider>
            </div >

        );
    }
}
