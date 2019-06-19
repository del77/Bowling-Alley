package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class CreateRegistrationException extends SsbdApplicationException {

    private static String code = "cannotCreateReservation";

    public CreateRegistrationException() {
        super();
    }

    public CreateRegistrationException(String message) {
        super(message);
    }

    public CreateRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateRegistrationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }

}
