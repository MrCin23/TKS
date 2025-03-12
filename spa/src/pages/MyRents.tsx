import { useEffect, useState } from 'react';
import axios from "axios";

interface EntityId {
    uuid: string;
}

interface VMachine {
    entityId: EntityId;
    ramSize: string;
    isRented: boolean;
    actualRentalPrice: number;
    cpunumber: number;
    cpumanufacturer: string | null;
}

interface ClientType {
    _clazz: string;
    entityId: EntityId;
    maxRentedMachines: number;
    name: string;
}

interface User {
    entityId: EntityId;
    firstName: string;
    surname: string;
    username: string;
    emailAddress: string;
    role: string;
    active: boolean;
    clientType: ClientType;
    currentRents: number;
}

interface Rent {
    entityId: EntityId;
    client: User;
    vmachine: VMachine;
    beginTime: Date;
    endTime: Date | null;
    rentCost: number;
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

export const MyRents = () => {
    const [token] = useState<string | null>(localStorage.getItem('token'));
    const [rents, setRents] = useState<Rent[]>([]);

    useEffect(() => {
        // Fetch rents for the current user
        const fetchRents = async () => {
            if (token) {
                try {
                    const response = await axios.get<Rent[]>(`/api/rent/all/client`,
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
                } catch (error) {
                    console.error('Error fetching rents:', error);
                }
            }
        };

        fetchRents();
    }, [token]);

    const handleEndRent = async (rentId: string) => {
        const confirmRent = window.confirm(
            `Czy na pewno chcesz zakończyć wypożyczenie o ID ${rentId}?`
        );
        if (!confirmRent) return;
        try {
            const token = localStorage.getItem("token");
            await axios.put(`/api/rent/end/${rentId}`,
                {},
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'ngrok-skip-browser-warning': '69420'
                    }
                });
            // Update rents after ending
            setRents((prevRents) => prevRents.map(rent =>
                rent.entityId.uuid === rentId ? { ...rent, endTime: new Date() } : rent
            ));
        } catch (error) {
            console.error('Error ending rent:', error);
        }
    };
    if (token == null) return <div className="text-center text-white mt-5">Musisz być zalogowany, aby przeglądać tę witrynę!</div>;

    return (
        <div className="container py-5">
            <h1 className="text-center text-white mb-4">My Rents</h1>
            {rents.length === 0 ? (
                <p className="text-center text-white">No rents found.</p>
            ) : (
                <div className="table-responsive">
                    <table className="table table-dark table-striped table-bordered">
                        <thead>
                        <tr>
                            <th>VM Name</th>
                            <th>RAM Size</th>
                            <th>CPU</th>
                            <th>Begin Time</th>
                            <th>End Time</th>
                            <th>Rent Cost</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {rents.map(rent => (
                            <tr key={rent.entityId.uuid}>
                                <td>{rent.vmachine.entityId.uuid}</td>
                                <td>{rent.vmachine.ramSize}</td>
                                <td>{`${rent.vmachine.cpunumber} - ${rent.vmachine.cpumanufacturer || 'Apple Arch'}`}</td>
                                <td>{new Date(rent.beginTime).toLocaleString()}</td>
                                <td>{rent.endTime ? new Date(rent.endTime).toLocaleString() : 'Active'}</td>
                                <td>{rent.rentCost}</td>
                                <td>
                                    {!rent.endTime && (
                                        <button className="btn btn-danger" onClick={() => handleEndRent(rent.entityId.uuid)}>End Rent</button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};
