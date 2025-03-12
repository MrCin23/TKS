import { ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { Pathnames } from '../../../router/pathnames';
import { FadingAlertComponent } from '../../alert/FadingAlert';
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

interface LayoutProps {
    children: ReactNode;
}

export const DefaultLayout = ({ children }: LayoutProps) => {
    // Funkcja nawigacji między widokami
    const navigate = useNavigate();

    return (
        <div className="bg-dark text-light"> {/* Ciemne tło dla całego layoutu */}
            {/* Pasek nawigacji */}
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                <div className="container-fluid">
                    <a
                        className="navbar-brand"
                        onClick={() => navigate(Pathnames.default.homePage)}
                        style={{ cursor: 'pointer' }}
                    >
                        MyApp
                    </a>
                    <button
                        className="navbar-toggler"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#navbarNav"
                        aria-controls="navbarNav"
                        aria-expanded="false"
                        aria-label="Toggle navigation"
                    >
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav ms-auto">
                            <li className="nav-item">
                                <button
                                    className="btn btn-outline-light mx-2"
                                    onClick={() => navigate(Pathnames.default.homePage)}
                                >
                                    Home
                                </button>
                            </li>
                            <li className="nav-item">
                                <button
                                    className="btn btn-outline-light mx-2"
                                    onClick={() => navigate(Pathnames.default.createUser)}
                                >
                                    Register
                                </button>
                            </li>
                            <li className="nav-item">
                                <button
                                    className="btn btn-outline-light mx-2"
                                    onClick={() => navigate(Pathnames.default.login)}
                                >
                                    Log In
                                </button>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            {/* Alert */}
            <div style={{ position: 'fixed', overflow: 'visible', width: 'inherit' }}>
                <FadingAlertComponent />
            </div>

            {/* Zawartość strony */}
            <div className="container mt-4">
                {children}
            </div>
        </div>
    );
};

