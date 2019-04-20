package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class EntityRetrievelException extends Exception {
    public EntityRetrievelException() {
    }

    public EntityRetrievelException(String message) {
        super(message);
    }

    public EntityRetrievelException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityRetrievelException(Throwable cause) {
        super(cause);
    }
}
