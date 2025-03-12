import {AlertColor} from "@mui/material";

/** Typ definiujący właściwości okna alert
 * visible - widoczność okna
 * title - tytuł okna
 * severity - "ważność" okna (tak naprawdę definiuje to jego kolor)
 * captionText - tekst do wyświetlenia w oknie
 */
export interface FadingAlertProps {
    visible: boolean,
    severity: AlertColor,
    title: string,
    captionText: string
}
