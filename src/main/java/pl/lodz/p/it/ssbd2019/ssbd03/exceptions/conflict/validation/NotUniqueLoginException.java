package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation;

public class NotUniqueLoginException extends ValidationException {

    public NotUniqueLoginException() {
        super();
    }

    public NotUniqueLoginException(String message) {
        super(message);
    }

    public NotUniqueLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniqueLoginException(Throwable cause) {
        super(cause);
    }
}
