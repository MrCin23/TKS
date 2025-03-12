import {FadingAlertProps} from "../../components/alert/FadingAlert/FadingAlertProps.ts";
import {createContext, ReactNode, useEffect, useState} from "react";

// Domyślne właściwości alertu (jedyne co jest istotne, to to, że jest on domyślnie niewidoczny)
const defaultAlertProps: FadingAlertProps = {visible: false, title: "", severity: "info", captionText: ""}

/** Definicja składowych kontekstu używanego do sterowania pojawianiem się alertu, który jest zdefiniowany dla całej aplikacji, a może być wywołany przez dowolny komponent.
 * Kontekst jest rodzajem stanu, który może być współdzielony między różnymi komponentami.
 * Propaguje on do poszczególnych komponentów za pośrednictwem providera.
 * @see FadingAlertContextProvider
 */
interface FadingAlertSharedState {
    alertProps: FadingAlertProps;
    setAlertProps: (newProps: FadingAlertProps) => void;
}

// Początkowy stan kontekstu to obiekt domyślnych właściwości alertu (w ten sposób jest on początkowo niewidoczny) oraz niezdefiniowana funkcja
// Kontekst udostępniany subkomponentom i tak zostanie ustawiony odrębnie przez FadingAlertContextProvider
const fadingAlertContext = createContext<FadingAlertSharedState>({alertProps:defaultAlertProps, setAlertProps:(newProps)=>{console.log(newProps)}});

interface ProviderChildren {
    children: ReactNode
}

/** Provider jest komponentem, który umożliwia propagowanie kontekstu do subkomponentów
 * Provider jest komponentem i definiuje swój stan używając useState. Ten stan to właściwości okna alert i to by wystarczyło do wyświetlenia takiego okna,
 * ale potrzebna jest też funkcja zamykająca okno po określonym czasie.
 * Dlatego provider udostępnia zarówno stan właściwości okna - z tego korzysta komponent AlertComponent,
 * jak i funkcję pushAlert() - z tego korzystają inne komponenty aby zaprezentować okno alertu.
 * Komponenty korzystają z kontekstu za pośrednictwem useAlert()
 * @see useAlertHook
 * @see FadingAlertComponent, FadingAlertProps
 * Komponent providera typowo jest stosowany wewnątrz komponentu App, tak, aby obejmował wszystkie inne komponenty
 * @see App
 * @param children - przyszłe subkomponenty providera
 */
export const FadingAlertContextProvider = ({children}:ProviderChildren) => {
    /** Stan komponentu providera, który będzie dostarczany do subkomponentów jako kontekst poprzez useAlert()
     * Komponent alertu użyje obiektu fadingAlertProps aby na jego podstawie pokazywać alert
     * @see FadingAlertComponent
     * Inne komponenty mogą użyć setFadingAlertProps, aby zmieniać stan i w ten sposób pokazywać nowe alerty.
     */
    const [fadingAlertProps, setFadingAlertProps] = useState<FadingAlertProps>(defaultAlertProps);

    /** useEffect umożliwia zdefiniowanie funkcji, która jest wywoływana w przypadku zmiany obserwowanego stanu (tu: fadingAlertProps - wszystkie składowe)
     * Jest to zatem rodzaj podłączenia funkcji zwrotnej (callback).
     */
    useEffect(() => {
        // Jeżeli alert jest widoczny, ustaw zdarzenie czasowe aby po okreslonym czasie ustawić nowy stan z visible:false i tym samym "zniknąć alert"
        if(fadingAlertProps.visible) {
            setTimeout(() => {
                // Jedyne co chcemy zmienić po upłynięciu czasu wyświetlania to stan visible. Inaczej alert zmieniłby swoją zawartość w trakcie wygaszania.
                setFadingAlertProps({visible:false, severity:fadingAlertProps.severity,title:fadingAlertProps.title,captionText:fadingAlertProps.captionText});
                }, 7000);
        }
    }, [fadingAlertProps])

    return (
        <fadingAlertContext.Provider value={{
            alertProps: fadingAlertProps,
            setAlertProps: setFadingAlertProps
        }}>
            {children}
        </fadingAlertContext.Provider>
    );
};

export default fadingAlertContext;