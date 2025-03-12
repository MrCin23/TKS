import { Navigate, Route, Routes } from 'react-router-dom';
import { defaultRoutes, adminRoutes, userRoutes, moderatorRoutes } from './routes.ts';
import { DefaultLayout } from "../components/layouts/default";
import { ClientLayout } from "../components/layouts/client";
import { AdminLayout } from "../components/layouts/admin";
import { ModeratorLayout } from "../components/layouts/moderator";
import { useAuth } from "../contexts/AuthContext.tsx";

export const RoutesComponent = () => {
    const { role } = useAuth();

    return (
        <Routes>
            {defaultRoutes.map(({ path, Component }) => (
                <Route key={path} path={path} element={<DefaultLayout><Component /></DefaultLayout>} />
            ))}
            {role === 'ADMIN' && adminRoutes.map(({ path, Component }) => (
                <Route key={path} path={path} element={<AdminLayout><Component /></AdminLayout>} />
            ))}
            {role === 'CLIENT' && userRoutes.map(({ path, Component }) => (
                <Route key={path} path={path} element={<ClientLayout><Component /></ClientLayout>} />
            ))}
            {role === 'RESOURCE_MANAGER' && moderatorRoutes.map(({ path, Component }) => (
                <Route key={path} path={path} element={<ModeratorLayout><Component /></ModeratorLayout>} />
            ))}
            <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
    );
};
