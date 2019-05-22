package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class ResetPasswordException extends Exception {
    public ResetPasswordException() {
    }

    public ResetPasswordException(String message) {
        super(message);
    }

    public ResetPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResetPasswordException(Throwable cause) {
        super(cause);
    }
}
