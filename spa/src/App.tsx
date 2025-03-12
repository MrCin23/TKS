import './App.css'

import {BrowserRouter as Router} from 'react-router-dom'
import {RoutesComponent} from "./router";
import {FadingAlertContextProvider} from "./contexts/FadingAlert/FadingAlertContext.tsx";
import {UserProvider} from "./model/UserContext.tsx";
import {AuthProvider} from "./contexts/AuthContext.tsx";
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

export const App = () => (
    // Dostarcza komponentom aplikacji kontekst, który umożliwia pokazywanie okna alertu
    //  podgląd wypożyczeń przez admina/moderatora (klikasz w Usera i widzisz jego wypożyczenia)
    // potwierdzanie wykonania ważnych operacji <- endRent, deleteVM, rentVM
    // daty z bazy
    <AuthProvider>
        <UserProvider>
            <FadingAlertContextProvider>
                <Router>
                    <RoutesComponent/>
                </Router>
            </FadingAlertContextProvider>
        </UserProvider>
    </AuthProvider>
)

export default App
