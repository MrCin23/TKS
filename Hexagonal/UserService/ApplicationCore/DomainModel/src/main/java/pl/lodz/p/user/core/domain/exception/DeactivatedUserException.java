package pl.lodz.p.user.core.domain.exception;

public class DeactivatedUserException extends RuntimeException {
    public DeactivatedUserException(String message) {
        super(message);
    }
}
