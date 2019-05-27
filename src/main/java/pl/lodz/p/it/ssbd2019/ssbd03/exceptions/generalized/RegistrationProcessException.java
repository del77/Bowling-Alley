package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class RegistrationProcessException extends SsbdApplicationException {

    private static String code = "cannotRegisterUser";

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

    @Override
    public String getCode(){
        return code;
    }
}
