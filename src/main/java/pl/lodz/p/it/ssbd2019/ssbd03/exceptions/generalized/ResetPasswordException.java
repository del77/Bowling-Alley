package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class ResetPasswordException extends SsbdApplicationException {

    private static String code = "cannotResetPassword";

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

    @Override
    public String getCode(){
        return code;
    }
}
