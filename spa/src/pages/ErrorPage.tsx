import React from "react";
import { useLocation } from "react-router-dom";

interface LocationState {
    state: { error?: Error };
}

const ErrorPage: React.FC = () => {
    const location = useLocation() as LocationState;
    const error = location.state?.error || new Error("An unknown error occurred" + location.state.error?.message);

    return <div>An error has occurred: {error.message}</div>;
};

export default ErrorPage;
