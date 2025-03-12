import React, { createContext, useContext, useState } from 'react';

enum Role {
    admin = "ADMIN",
    resourcemanager = "RESOURCE_MANAGER",
    client = "CLIENT",
}

interface EntityId {
    uuid: string;
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
    role: Role; // Use the Role enum here
    active: boolean;
    clientType: ClientType | null; // Align with ModelUserProfile's nullable type
    currentRents: number | null;  // Align with ModelUserProfile's nullable type
}

const UserContext = createContext<{
    currentUser: User | null;
    setCurrentUser: (user: User) => void;
    clearUser: () => void;
}>({
    currentUser: null,
    setCurrentUser: () => {},
    clearUser: () => {},
});

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [currentUser, setCurrentUserState] = useState<User | null>(null);

    const setCurrentUser = (user: User) => {
        setCurrentUserState(user);
    };

    const clearUser = () => {
        setCurrentUserState(null);
    };

    return (
        <UserContext.Provider value={{ currentUser, setCurrentUser, clearUser }}>
            {children}
        </UserContext.Provider>
    );
};

// Hook for easier access to the context
export const useUserSession = () => {
    return useContext(UserContext);
};
