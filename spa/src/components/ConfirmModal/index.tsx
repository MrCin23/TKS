import {BaseModalComponent, BaseModalProps} from "../BaseModal";

export interface ConfirmModalProps extends Omit<BaseModalProps, 'children'> {
    captionText: string
}
export const ConfirmModalComponent = ({
                                                open,
                                                title,
                                                captionText,
                                                handleClose,
                                                handleConfirm,
                                            }: ConfirmModalProps) => {

    return (
        <BaseModalComponent
            title={title}
            open={open}
            handleClose={handleClose}
            handleConfirm={handleConfirm}
        >
            <div>{captionText}</div>
        </BaseModalComponent>
    )
}