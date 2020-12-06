import { Button, List, ListItem, ListItemIcon, ListItemText, Paper, ThemeProvider } from '@material-ui/core';
import NotificationsIcon from '@material-ui/icons/Notifications';
import React from 'react';
import { getTheme } from '../util/util';
import InboxIcon from '@material-ui/icons/Inbox';
import DraftsIcon from '@material-ui/icons/Drafts';

export default class Notifications extends React.Component {

    state = {
        hideNotifications: true
    }

    toggleNotifications() {
        this.setState({
            hideNotifications: !this.state.hideNotifications
        });
    }

    render() {
        return (
            <div className="centeredSpan align-self">
                <ThemeProvider theme={getTheme()}>
                    <Button onClick={() => this.toggleNotifications()}>
                        <NotificationsIcon></NotificationsIcon>
                    </Button>
                    <div className={"notification-panel " + (this.state.hideNotifications ? 'hide' : '')} >
                        <Paper elevation={3}>
                            <List component="nav" aria-label="main mailbox folders">
                                <ListItem button>
                                    <ListItemIcon>
                                        <InboxIcon />
                                    </ListItemIcon>
                                    <ListItemText primary="Inbox" />
                                </ListItem>
                                <ListItem button>
                                    <ListItemIcon>
                                        <DraftsIcon />
                                    </ListItemIcon>
                                    <ListItemText primary="Drafts" />
                                </ListItem>
                            </List>
                        </Paper>
                    </div>
                </ThemeProvider>
            </div >

        );
    }
}
