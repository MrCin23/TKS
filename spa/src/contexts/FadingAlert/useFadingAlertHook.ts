import { useContext } from 'react';
import fadingAlertContext from './FadingAlertContext.tsx';

/**
 * Udostępnia stan fadingAlertContext komponentom będącym w zasięgu FadingAlertContextProvidera
 * @see FadingAlertContextProvider
 */
export const useFadingAlert = () => {
    return useContext(fadingAlertContext);
}
