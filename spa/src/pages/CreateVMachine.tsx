import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

interface AppleArch {
    ramSize: string;
    cpunumber: number;
    _clazz: string;
}

interface x86 {
    ramSize: string;
    cpunumber: number;
    cpumanufacturer: string;
    _clazz: string;
}

type FormData = AppleArch | x86;

export const CreateVMachine = () => {
    const [token] = useState<string | null>(localStorage.getItem('token'));
    const navigate = useNavigate();
    const [formData, setFormData] = useState<FormData>({
        ramSize: '',
        cpunumber: 0,
        _clazz: 'applearch', // Domyślny typ
        cpumanufacturer: '', // Tylko dla x86
    });

    const [ramAmount, setRamAmount] = useState<number>(0);
    const [ramUnit, setRamUnit] = useState<string>('GB'); // Domyślna jednostka
    const [formErrors, setFormErrors] = useState<{ [key: string]: string }>({});
    const [notification, setNotification] = useState<string | null>(null);

    const validate = (): boolean => {
        const errors: { [key: string]: string } = {};

        if (ramAmount <= 0) {
            errors.ramSize = 'RAM amount must be greater than 0.';
        }

        if (formData.cpunumber <= 0) {
            errors.cpunumber = 'CPU number must be greater than 0.';
        }

        if (formData._clazz === 'x86' && !('cpumanufacturer' in formData && formData.cpumanufacturer)) {
            errors.cpumanufacturer = 'CPU manufacturer is required for x86.';
        }

        setFormErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;

        if (name === 'ramAmount') {
            setRamAmount(Number(value));
        } else if (name === 'ramUnit') {
            setRamUnit(value);
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const handleTypeChange = (type: 'applearch' | 'x86') => {
        setFormData({
            ramSize: '',
            cpunumber: 0,
            _clazz: type,
            ...(type === 'x86' ? { cpumanufacturer: '' } : {}),
        });
        setFormErrors({});
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validate()) {
            setNotification('There are errors in the form.');
            return;
        }
        const confirmation = window.confirm(
            `Czy na pewno chcesz zarejestrować maszynę wirtualną?`
        );
        if (!confirmation) return;
        // Połącz ilość RAM i jednostkę
        const ramSize = `${ramAmount}${ramUnit}`;
        const payload = { ...formData, ramSize };

        try {
            await axios.post('/api/vmachine', payload,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'ngrok-skip-browser-warning': '69420'
                    }
                });
            setNotification('VMachine registered successfully!');
            setFormData({
                ramSize: '',
                cpunumber: 0,
                _clazz: 'applearch',
            });
            setRamAmount(0);
            setRamUnit('GB');
            setFormErrors({});
        } catch (error) {
            console.error(error);
            navigate('/error', { state: { error } });
        }
    };

    return (
        <div className="container mt-5">
            <h2 className="text-center text-light">VMachine Registration</h2>
            {notification && <p className={`alert ${notification.includes('success') ? 'alert-success' : 'alert-danger'}`}>{notification}</p>}
            <div className="d-flex justify-content-center mb-4">
                <button className="btn btn-primary mx-2" onClick={() => handleTypeChange('applearch')}>AppleArch</button>
                <button className="btn btn-primary mx-2" onClick={() => handleTypeChange('x86')}>x86</button>
            </div>
            <form onSubmit={handleSubmit} className="shadow p-4 rounded bg-dark text-light">
                <div className="mb-3">
                    <label htmlFor="ramAmount" className="form-label">RAM</label>
                    <div className="d-flex gap-2">
                        <input
                            type="number"
                            id="ramAmount"
                            name="ramAmount"
                            value={ramAmount}
                            onChange={handleChange}
                            className="form-control bg-dark text-light"
                        />
                        <select
                            id="ramUnit"
                            name="ramUnit"
                            value={ramUnit}
                            onChange={handleChange}
                            className="form-select bg-dark text-light"
                        >
                            <option value="MB">MB</option>
                            <option value="GB">GB</option>
                            <option value="TB">TB</option>
                        </select>
                    </div>
                    {formErrors.ramSize && <span className="text-danger">{formErrors.ramSize}</span>}
                </div>

                <div className="mb-3">
                    <label htmlFor="cpunumber" className="form-label">CPU Number</label>
                    <input
                        type="number"
                        id="cpunumber"
                        name="cpunumber"
                        value={formData.cpunumber}
                        onChange={handleChange}
                        className="form-control bg-dark text-light"
                    />
                    {formErrors.cpunumber && <span className="text-danger">{formErrors.cpunumber}</span>}
                </div>

                {formData._clazz === 'x86' && (
                    <div className="mb-3">
                        <label htmlFor="cpumanufacturer" className="form-label">CPU Manufacturer</label>
                        <input
                            type="text"
                            id="cpumanufacturer"
                            name="cpumanufacturer"
                            value={(formData as x86).cpumanufacturer || ''}
                            onChange={handleChange}
                            className="form-control bg-dark text-light"
                        />
                        {formErrors.cpumanufacturer && <span className="text-danger">{formErrors.cpumanufacturer}</span>}
                    </div>
                )}

                <button type="submit" className="btn btn-success w-100">
                    Register
                </button>
            </form>
        </div>
    );
};
