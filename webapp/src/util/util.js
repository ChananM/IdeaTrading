import { createMuiTheme } from "@material-ui/core";

export function getTheme() {
    return createMuiTheme({
        palette: {
            type: "dark"
        }
    });
}