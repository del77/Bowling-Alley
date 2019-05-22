package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class EmailDoesNotExistException extends Exception {
    public EmailDoesNotExistException() { }

    public EmailDoesNotExistException(String message) {
        super(message);
    }

    public EmailDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
