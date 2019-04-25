package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class ChangePasswordException extends Exception {
    public ChangePasswordException() {
    }

    public ChangePasswordException(String message) {
        super(message);
    }

    public ChangePasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChangePasswordException(Throwable cause) {
        super(cause);
    }
}
