package pl.lodz.p.exception;

public class DeactivatedUserException extends RuntimeException {
    public DeactivatedUserException(String message) {
        super(message);
    }
}
