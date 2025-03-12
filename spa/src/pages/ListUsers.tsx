import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './styles.css'; // Dołączysz swój plik CSS na później, aby ustawić ciemny motyw

interface EntityId {
    uuid: string;
}

interface ClientType {
    _clazz: string;
    entityId: EntityId;
    maxRentedMachines: number;
    name: string;
}

interface VMachine {
    entityId: EntityId;
    ramSize: string;
    isRented: boolean;
    actualRentalPrice: number;
    cpunumber: number;
    cpumanufacturer: string | null;
}

interface Rent {
    entityId: EntityId;
    client: User;
    vmachine: VMachine;
    beginTime: Date;
    endTime: Date | null;
    rentCost: number;
}

interface User {
    entityId: EntityId;
    firstName: string;
    surname: string;
    username: string;
    emailAddress: string;
    role: string;
    active: boolean;
    clientType: ClientType | undefined | null;
    currentRents: number | undefined | null;
}

function convertToDate(input: Date | Array<number>): Date {
    if (input instanceof Date) {
        return input;
    } else if (Array.isArray(input)) {
        const [year, month, day, hour = 0, minute = 0, second = 0, nanoseconds = 0] = input;
        const milliseconds = nanoseconds / 1e6;
        return new Date(year, month - 1, day, hour, minute, second, milliseconds);
    } else {
        throw new TypeError("Input must be a Date or an array of numbers.");
    }
}

export const ListUsers = () => {
    const [token] = useState<string | null>(localStorage.getItem('token'));
    const [clients, setUsers] = useState<User[]>([]);
    const [username, setUsername] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [rents, setRents] = useState<Rent[] | null>(null);
    const [currentUser, setCurrentUser] = useState<User | null>(null);

    const handleChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const newUsername = e.target.value;
        setUsername(newUsername);

        try {
            if (newUsername === '') {
                const response = await axios.get<User[]>('/api/client',
                    {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'ngrok-skip-browser-warning': '69420'
                        }
                    });
                setUsers(response.data);
                setError(null);
            } else {
                const response = await axios.get<User[]>(`/api/client/findClients/${newUsername}`,
                    {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'ngrok-skip-browser-warning': '69420'
                        }
                    });
                if (response.data.length === 0) {
                    setError(`Nie znaleziono użytkownika o nazwie "${newUsername}".`);
                    setUsers([]);
                } else {
                    setUsers(response.data);
                    setError(null);
                }
            }
        } catch (err) {
            setError(`Wystąpił błąd podczas wyszukiwania użytkownika ${err}. Spróbuj ponownie później.`);
            setUsers([]);
        }
    };

    const activate = async (activate: boolean, entityId: string) => {
        try {
            if (activate) {
                const confirmation = window.confirm(
                    `Czy na pewno chcesz aktywować użytkownika o ID ${entityId}?`
                );
                if (!confirmation) return;
                await axios.put(`/api/client/activate/${entityId}`,
                    {},
                    {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'ngrok-skip-browser-warning': '69420'
                        }
                    });
                alert(`Klient o ID ${entityId} aktywowany!`);
            } else {
                const confirmation = window.confirm(
                    `Czy na pewno chcesz dezaktywować użytkownika o ID ${entityId}?`
                );
                if (!confirmation) return;
                await axios.put(`/api/client/deactivate/${entityId}`,
                    {},
                    {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'ngrok-skip-browser-warning': '69420'
                        }
                    });
                alert(`Klient o ID ${entityId} deaktywowany!`);
            }
            setUsers((prev) =>
                prev.map((user) =>
                    user.entityId.uuid === entityId ? { ...user, active: activate } : user
                )
            );
        } catch (err) {
            console.error("Błąd przy deaktywowaniu użytkownika:", err);
            alert("Nie udało się deaktywować użytkownika. Spróbuj ponownie później.");
        }
    };

    const fetchRents = async (user: User) => {
        try {
            const response = await axios.get<Rent[]>(`/api/rent/all/client/${user.entityId.uuid}`,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'ngrok-skip-browser-warning': '69420'
                    }
                });

            setRents(response.data.map(rent => ({
                ...rent,
                beginTime: convertToDate(rent.beginTime),
                endTime: rent.endTime ? convertToDate(rent.endTime) : null,
            })));
            console.log(response.data)
            setCurrentUser(user);
        } catch (err) {
            alert(`Nie udało się pobrać listy wypożyczeń ${err}. Spróbuj ponownie później.`);
        }
    };

    const closeRentsModal = () => {
        setRents(null);
        setCurrentUser(null);
    };

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get<User[]>('/api/client',
                    {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'ngrok-skip-browser-warning': '69420'
                        }
                    });
                setUsers(response.data);
                setLoading(false);
            } catch (err) {
                setError('Nie udało się pobrać listy klientów. Spróbuj ponownie później.' + err);
                setLoading(false);
            }
        };
        fetchUsers();
    }, []);

    if (loading) return <div>Ładowanie...</div>;

    return (
        <div className="container mt-4 text-light bg-dark p-4 rounded">
            <h1 className="mb-4 text-center">Lista Klientów</h1>
            <input
                type="text"
                className="form-control mb-4"
                id="username"
                name="username"
                placeholder="Wyszukaj po nazwie użytkownika"
                value={username}
                onChange={handleChange}
            />
            {error && <div className="alert alert-danger">{error}</div>}
            <div className="table-responsive" style={{ maxHeight: '400px', overflowY: 'auto' }}>
                <table className="table table-striped table-bordered table-dark">
                    <thead>
                    <tr>
                        <th>Imię</th>
                        <th>Nazwisko</th>
                        <th>Nazwa użytkownika</th>
                        <th>Adres e-mail</th>
                        <th>Rola</th>
                        <th>Typ klienta</th>
                        <th>Aktywacja</th>
                        <th>Wypożyczenia</th>
                    </tr>
                    </thead>
                    <tbody>
                    {clients.map((client) => (
                        <tr key={client.entityId.uuid}>
                            <td>{client.firstName}</td>
                            <td>{client.surname}</td>
                            <td>{client.username}</td>
                            <td>{client.emailAddress}</td>
                            <td>{client.role}</td>
                            <td>{client.clientType?.name}</td>
                            <td>
                                {client.active ? (
                                    <button className="btn btn-danger" onClick={() => activate(false, client.entityId.uuid)}>
                                        Deaktywuj
                                    </button>
                                ) : (
                                    <button className="btn btn-success" onClick={() => activate(true, client.entityId.uuid)}>
                                        Aktywuj
                                    </button>
                                )}
                            </td>
                            <td>
                                <button className="btn btn-info" onClick={() => fetchRents(client)}>
                                    Pokaż wypożyczenia
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {rents && (
                <div className="modal d-block">
                    <div className="modal-dialog">
                        <div className="modal-content bg-dark text-light">
                            <div className="modal-header">
                                <button className="close text-light" onClick={closeRentsModal}>X</button>
                                <h5 className="modal-title">Wypożyczenia użytkownika {currentUser?.firstName} {currentUser?.surname}</h5>
                            </div>
                            <div className="modal-body">
                                <div className="table-responsive" style={{ maxHeight: '300px', overflowY: 'auto' }}>
                                    <table className="table table-striped table-dark">
                                        <thead>
                                        <tr>
                                            <th>Maszyna</th>
                                            <th>Data początkowa</th>
                                            <th>Data końcowa</th>
                                            <th>Cena</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {rents.map((rent) => (
                                            <tr key={rent.entityId.uuid}>
                                                <td>{rent.vmachine.ramSize} GB</td>
                                                <td>{rent.beginTime.toLocaleString()}</td>
                                                <td>{rent.endTime?.toLocaleString() || "Brak"}</td>
                                                <td>{rent.rentCost}</td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};
