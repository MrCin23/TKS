import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
// import { useUserSession } from '../model/UserContext';
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import {jwtDecode} from "jwt-decode";
// import {Pathnames} from "../router/pathnames.ts";

enum Role {
    admin = "ADMIN",
    moderator = "RESOURCE_MANAGER",
    client = "CLIENT",
}

interface ClientType {
    _clazz: "standard" | "premium";
}

interface FormData {
    _clazz: string;
    firstName: string;
    surname: string;
    username: string;
    password: string;
    emailAddress: string;
    role: Role;
    active: string;
    clientType: ClientType;
}

export const CreateUser = () => {
    const navigate = useNavigate();
    // const { currentUser } = useUserSession();
    const [formData, setFormData] = useState<FormData>({
        _clazz: 'Client',
        firstName: '',
        surname: '',
        username: '',
        password: '',
        emailAddress: '',
        role: Role.client,
        active: true.toString(),
        clientType: {_clazz: "standard"},
    });

    const [formErrors, setFormErrors] = useState<{ [key: string]: string }>({});
    const [notification, setNotification] = useState<string | null>(null);

    const validate = (): boolean => {
        const errors: { [key: string]: string } = {};

        if (!formData.firstName.trim()) {
            errors.firstName = 'First name is required';
        }
        if (!formData.surname.trim()) {
            errors.surname = 'Last name is required';
        }
        if (!formData.username.trim()) {
            errors.username = 'Username is required';
        }
        if (!formData.password.trim()) {
            errors.username = 'Password is required';
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!formData.emailAddress.trim()) {
            errors.emailAddress = 'Email is required';
        } else if (!emailRegex.test(formData.emailAddress)) {
            errors.emailAddress = 'Invalid email address';
        }
        setFormErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;

        const updatedFormData = { ...formData, [name]: value };

        if (name === "_clazz") {
            switch (value) {
                case "Admin":
                    updatedFormData.role = Role.admin;
                    break;
                case "ResourceManager":
                    updatedFormData.role = Role.moderator;
                    break;
                default:
                    updatedFormData.role = Role.client;
            }
        }

        setFormData(updatedFormData);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validate()) {
            setNotification('There are errors in the form.');
            return;
        }
        const confirmation = window.confirm(
            `Czy na pewno chcesz utworzyć użytkownika?`
        );
        if (!confirmation) return;
        try {
            console.log(formData);
            await axios.post('/api/client', formData);
                // {
                //     headers: {
                //         'ngrok-skip-browser-warning': '69420'
                //     }
                // });
            setNotification('User registered successfully!');
            setFormData({
                _clazz: 'Client',
                firstName: '',
                surname: '',
                username: '',
                password: '',
                emailAddress: '',
                role: Role.client,
                active: true.toString(),
                clientType: {_clazz: "standard"},
            });
            setFormErrors({});
        } catch (error) {
            console.log(error);
            navigate("/error", { state: { error } });
        }
    };
    const token = localStorage.getItem('token');
    let decodedToken: any
    if (token) {
        // Dekodowanie tokenu
        decodedToken = jwtDecode(token);

        if (decodedToken.role == "ADMIN") {
            return (
                <div className="container" style={{ maxWidth: '400px' }}>
                    <h2 className="mb-4">User Registration</h2>
                    {notification && (
                        <div className={`alert ${notification.includes('success') ? 'alert-success' : 'alert-danger'}`} role="alert">
                            {notification}
                        </div>
                    )}
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="firstName" className="form-label">First Name</label>
                            <input
                                type="text"
                                id="firstName"
                                name="firstName"
                                value={formData.firstName}
                                onChange={handleChange}
                                className="form-control"
                            />
                            {formErrors.firstName && <div className="text-danger">{formErrors.firstName}</div>}
                        </div>

                        <div className="mb-3">
                            <label htmlFor="surname" className="form-label">Surname</label>
                            <input
                                type="text"
                                id="surname"
                                name="surname"
                                value={formData.surname}
                                onChange={handleChange}
                                className="form-control"
                            />
                            {formErrors.surname && <div className="text-danger">{formErrors.surname}</div>}
                        </div>

                        <div className="mb-3">
                            <label htmlFor="username" className="form-label">Username</label>
                            <input
                                type="text"
                                id="username"
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                                className="form-control"
                            />
                            {formErrors.username && <div className="text-danger">{formErrors.username}</div>}
                        </div>

                        <div className="mb-3">
                            <label htmlFor="emailAddress" className="form-label">Email address</label>
                            <input
                                type="email"
                                id="emailAddress"
                                name="emailAddress"
                                value={formData.emailAddress}
                                onChange={handleChange}
                                className="form-control"
                            />
                            {formErrors.emailAddress && <div className="text-danger">{formErrors.emailAddress}</div>}
                        </div>

                        <div className="mb-3">
                            <label htmlFor="password" className="form-label">Password</label>
                            <input
                                type="password"
                                id="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                className="form-control"
                            />
                            {formErrors.password && <div className="text-danger">{formErrors.password}</div>}
                        </div>

                        <div className="mb-3">
                            <label htmlFor="_clazz" className="form-label">Role</label>
                            <select
                                id="_clazz"
                                name="_clazz"
                                value={formData._clazz}
                                onChange={handleChange}
                                className="form-select"
                            >
                                <option value="Client">Client</option>
                                <option value="ResourceManager">Resource Manager</option>
                                <option value="Admin">Admin</option>
                            </select>
                        </div>

                        <button type="submit" className="btn btn-primary w-100">Register</button>
                    </form>
                </div>
            );
        }

    }

    return (
        <div className="container" style={{maxWidth: '400px'}}>
            <h2 className="mb-4">User Registration</h2>
            {notification && (
                <div className={`alert ${notification.includes('success') ? 'alert-success' : 'alert-danger'}`}
                     role="alert">
                    {notification}
                </div>
            )}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="firstName" className="form-label">First Name</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        className="form-control"
                    />
                    {formErrors.firstName && <div className="text-danger">{formErrors.firstName}</div>}
                </div>

                <div className="mb-3">
                    <label htmlFor="surname" className="form-label">Surname</label>
                    <input
                        type="text"
                        id="surname"
                        name="surname"
                        value={formData.surname}
                        onChange={handleChange}
                        className="form-control"
                    />
                    {formErrors.surname && <div className="text-danger">{formErrors.surname}</div>}
                </div>

                <div className="mb-3">
                    <label htmlFor="username" className="form-label">Username</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        className="form-control"
                    />
                    {formErrors.username && <div className="text-danger">{formErrors.username}</div>}
                </div>

                <div className="mb-3">
                    <label htmlFor="emailAddress" className="form-label">Email address</label>
                    <input
                        type="email"
                        id="emailAddress"
                        name="emailAddress"
                        value={formData.emailAddress}
                        onChange={handleChange}
                        className="form-control"
                    />
                    {formErrors.emailAddress && <div className="text-danger">{formErrors.emailAddress}</div>}
                </div>

                <div className="mb-3">
                    <label htmlFor="password" className="form-label">Password</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        className="form-control"
                    />
                    {formErrors.password && <div className="text-danger">{formErrors.password}</div>}
                </div>

                <button type="submit" className="btn btn-primary w-100">Register</button>
            </form>
        </div>
    );
};
