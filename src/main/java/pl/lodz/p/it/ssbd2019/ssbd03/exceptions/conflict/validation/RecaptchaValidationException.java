package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation;

public class RecaptchaValidationException extends ValidationException {

    public RecaptchaValidationException(String message) {
        super(message);
    }

    public RecaptchaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
