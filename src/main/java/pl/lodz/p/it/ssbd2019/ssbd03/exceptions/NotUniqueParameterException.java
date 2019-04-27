package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class NotUniqueParameterException extends Exception {
    public NotUniqueParameterException() {
        super();
    }

    public NotUniqueParameterException(String message) {
        super(message);
    }

    public NotUniqueParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniqueParameterException(Throwable cause) {
        super(cause);
    }
}
