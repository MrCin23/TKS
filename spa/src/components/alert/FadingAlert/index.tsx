import { useFadingAlert } from '../../../contexts/FadingAlert/useFadingAlertHook.ts';
import Fade from "@mui/material/Fade";
import Alert from "@mui/material/Alert";
import AlertTitle from "@mui/material/AlertTitle";

export const FadingAlertComponent = () => {
    const { alertProps } = useFadingAlert();

        return (
            <div>
                <Fade in={alertProps.visible} timeout={{enter: 1000, exit: 1000}}>
                    <Alert severity={alertProps.severity} variant="standard" className="alert">
                        <AlertTitle>{alertProps.title}</AlertTitle>
                        {alertProps.captionText}
                    </Alert>
                </Fade>
            </div>
        )
};