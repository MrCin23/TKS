import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import './styles.css';

interface EntityId {
    uuid: string;
}

interface ClientType {
    _clazz: string;
    entityId: EntityId;
    maxRentedMachines: number;
    name: string;
}

enum Role {
    admin = 'ADMIN',
    resourcemanager = 'RESOURCE_MANAGER',
    client = 'CLIENT',
}

interface User {
    entityId: EntityId;
    firstName: string;
    surname: string;
    username: string;
    emailAddress: string;
    role: Role;
    active: boolean;
    clientType: ClientType;
    currentRents: number;
}

export const EditProfile = () => {
    const [token] = useState<string | null>(localStorage.getItem('token'));
    const [userData, setUserData] = useState<User | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [etag, setEtag] = useState<string | null>(null); // New state to store the ETag

    const [formData, setFormData] = useState({
        firstName: '',
        surname: '',
        emailAddress: '',
    });

    const [passwordData, setPasswordData] = useState({
        oldPassword: '',
        newPassword: '',
    });

    useEffect(() => {
        const fetchUserData = async () => {
            if (!token) return;

            try {
                const decodedToken: any = jwtDecode(token);
                const username = decodedToken.sub;
                const response = await axios.get<User>(`/api/client/findClient/${username}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'ngrok-skip-browser-warning': '69420',
                    },
                    // Capture the ETag from the response headers
                });

                setUserData(response.data);
                setFormData({
                    firstName: response.data.firstName,
                    surname: response.data.surname,
                    emailAddress: response.data.emailAddress,
                });
                setEtag(response.headers['etag']); // Store ETag
                setLoading(false);
            } catch (err) {
                setError(`Nie udało się pobrać danych użytkownika: ${err}`);
                setLoading(false);
            }
        };

        fetchUserData();
    }, [token]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setPasswordData((prev) => ({ ...prev, [name]: value }));
    };

    const handleProfileUpdate = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!token || !userData) {
            alert('Musisz być zalogowany, aby edytować swój profil.');
            return;
        }

        const confirmation = window.confirm('Czy na pewno chcesz zmienić dane?');
        if (!confirmation) return;

        try {
            await axios.put(`/api/client/${userData.entityId.uuid}`, formData, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'ngrok-skip-browser-warning': '69420',
                    'If-Match': etag || '', // Include the stored ETag
                },
            });
            alert('Dane zostały zaktualizowane!');
        } catch (err) {
            console.error('Błąd przy aktualizacji danych:', err);
            alert('Nie udało się zaktualizować danych. Spróbuj ponownie.');
        }
    };

    const handlePasswordUpdate = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!token || !userData) {
            alert('Musisz być zalogowany, aby zmienić hasło.');
            return;
        }

        if (passwordData.newPassword.length < 6) {
            alert('Nowe hasło musi mieć co najmniej 6 znaków.');
            return;
        }

        const confirmation = window.confirm('Czy na pewno chcesz zmienić hasło?');
        if (!confirmation) return;

        try {
            await axios.put(
                `/api/client/changePassword`,
                {
                    username: userData.username,
                    oldPassword: passwordData.oldPassword,
                    newPassword: passwordData.newPassword,
                },
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'ngrok-skip-browser-warning': '69420',
                    },
                }
            );
            alert('Hasło zostało zmienione!');
        } catch (err) {
            console.error('Błąd przy zmianie hasła:', err);
            alert('Nie udało się zmienić hasła. Sprawdź poprawność starego hasła i spróbuj ponownie.');
        }
    };

    if (loading) return <div>Ładowanie danych...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div className="container">
            <h1 className="text-center mb-4">Edycja profilu</h1>

            {userData && (
                <form className="edit-profile-form" onSubmit={handleProfileUpdate}>
                    <div className="mb-3">
                        <label htmlFor="firstName" className="form-label">
                            Imię:
                        </label>
                        <input
                            type="text"
                            id="firstName"
                            name="firstName"
                            value={formData.firstName}
                            onChange={handleInputChange}
                            className="form-control"
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="surname" className="form-label">
                            Nazwisko:
                        </label>
                        <input
                            type="text"
                            id="surname"
                            name="surname"
                            value={formData.surname}
                            onChange={handleInputChange}
                            className="form-control"
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="emailAddress" className="form-label">
                            Adres e-mail:
                        </label>
                        <input
                            type="email"
                            id="emailAddress"
                            name="emailAddress"
                            value={formData.emailAddress}
                            onChange={handleInputChange}
                            className="form-control"
                            required
                        />
                    </div>
                    <button className="btn btn-primary w-100" type="submit">
                        Zapisz zmiany
                    </button>
                </form>
            )}

            <h2 className="text-center mt-5">Zmiana hasła</h2>
            <form className="edit-profile-form" onSubmit={handlePasswordUpdate}>
                <div className="mb-3">
                    <label htmlFor="oldPassword" className="form-label">
                        Stare hasło:
                    </label>
                    <input
                        type="password"
                        id="oldPassword"
                        name="oldPassword"
                        value={passwordData.oldPassword}
                        onChange={handlePasswordChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="newPassword" className="form-label">
                        Nowe hasło:
                    </label>
                    <input
                        type="password"
                        id="newPassword"
                        name="newPassword"
                        value={passwordData.newPassword}
                        onChange={handlePasswordChange}
                        className="form-control"
                        required
                    />
                </div>
                <button className="btn btn-danger w-100" type="submit">
                    Zmień hasło
                </button>
            </form>
        </div>
    );
};
