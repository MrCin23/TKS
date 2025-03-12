import { Button, Modal, Typography } from '@mui/material'
import { ReactElement } from 'react'
import { ModalBody, ModalContent, ModalFooter, ModalHeader } from './styles'

/** Typ definiujący właściwości okna modal
 * title - tytuł okna
 * open - widoczność okna
 * handleConfirm - funkcja wywoływana po użyciu przycisku potwierdzenia
 * handleClose - funkcja wywoływana po użyciu przycisku potwierdzenia
 * children - subkomponent będący "wnętrzem" (zawartością pomiędzy tytułem a przyciskami) okna
 */
export interface BaseModalProps {
    title?: string
    open: boolean
    handleConfirm: () => void
    handleClose: () => void
    children: ReactElement
}

export const BaseModalComponent = ({
                                   title,
                                   open,
                                   handleConfirm,
                                   handleClose,
                                   children,
                               }: BaseModalProps) => {
    return (
        <Modal
            open={open}
            onClose={handleClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <ModalContent>
                {title && (
                    <ModalHeader>
                        <Typography variant="h5">{title}</Typography>
                    </ModalHeader>
                )}
                <ModalBody>{children}</ModalBody>
                <ModalFooter>
                    <Button variant="outlined" onClick={handleClose} color="error">
                        Close
                    </Button>
                    <Button variant="contained" onClick={handleConfirm}>
                        Confirm
                    </Button>
                </ModalFooter>
            </ModalContent>
        </Modal>
    )
}