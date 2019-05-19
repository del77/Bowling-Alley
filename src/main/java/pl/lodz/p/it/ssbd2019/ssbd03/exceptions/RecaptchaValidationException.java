package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class RecaptchaValidationException extends Exception {
    public RecaptchaValidationException(String message) {
        super(message);
    }

    public RecaptchaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
