package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class RegistrationProcessException extends Exception {
    public RegistrationProcessException() {
    }

    public RegistrationProcessException(String message) {
        super(message);
    }

    public RegistrationProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationProcessException(Throwable cause) {
        super(cause);
    }
}
