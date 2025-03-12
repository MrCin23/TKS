import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { Pathnames } from "../router/pathnames.ts";
import { jwtDecode } from "jwt-decode";
import { useAuth } from "../contexts/AuthContext.tsx";

export const LogInUser = () => {
    const { setToken } = useAuth();
    const [loginForm, setLoginForm] = useState({ username: '', password: '' });
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setLoginForm({ ...loginForm, [name]: value });
    };

    const handleLogIn = async () => {
        if (!loginForm.username.trim() || !loginForm.password.trim()) {
            setError('Username and Password are required');
            return;
        }

        try {
            const response = await axios.post<string>('/api/client/login', loginForm, {
                headers: { 'ngrok-skip-browser-warning': '69420' }
            });
            if (response.status === 403) {
                setError(response.data);
            }
            console.log("Token received:", response.data);
            setToken(response.data); // Update context

            const decodedToken: any = jwtDecode(response.data);
            console.log("Decoded Token:", decodedToken);

            if (decodedToken.role === "CLIENT") {
                navigate(Pathnames.user.homePage);
            } else if (decodedToken.role === "ADMIN") {
                navigate(Pathnames.admin.homePage);
            } else if (decodedToken.role === "RESOURCE_MANAGER") {
                navigate(Pathnames.moderator.homePage);
            }

        } catch (err) {
            console.error(err);
            setError('Invalid credentials or server error.');
        }
    };

    return (
        <div className="container">
            <h2>Log In</h2>
            <input type="text" name="username" placeholder="Username" value={loginForm.username} onChange={handleChange} />
            <input type="password" name="password" placeholder="Password" value={loginForm.password} onChange={handleChange} />
            <button onClick={handleLogIn}>Log In</button>
            {error && <p className="error">{error}</p>}
        </div>
    );
};
