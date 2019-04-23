package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class EntityUpdateException extends Exception {
    public EntityUpdateException() {
    }

    public EntityUpdateException(String message) {
        super(message);
    }

    public EntityUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityUpdateException(Throwable cause) {
        super(cause);
    }
}
