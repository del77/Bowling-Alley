package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation;

public class NotUniqueEmailException extends ValidationException {

    public NotUniqueEmailException() {
        super();
    }

    public NotUniqueEmailException(String message) {
        super(message);
    }

    public NotUniqueEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniqueEmailException(Throwable cause) {
        super(cause);
    }

}
