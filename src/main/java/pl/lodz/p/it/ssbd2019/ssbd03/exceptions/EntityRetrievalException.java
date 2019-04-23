package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class EntityRetrievalException extends Exception {
    public EntityRetrievalException() {
    }

    public EntityRetrievalException(String message) {
        super(message);
    }

    public EntityRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityRetrievalException(Throwable cause) {
        super(cause);
    }
}
