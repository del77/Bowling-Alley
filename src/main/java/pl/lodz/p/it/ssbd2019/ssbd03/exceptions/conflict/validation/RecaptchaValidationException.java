package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation;

public class RecaptchaValidationException extends ValidationException {

    private static String code = "validate.recaptchaNotPerformed";

    public RecaptchaValidationException(String message) {
        super(message);
    }

    public RecaptchaValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
