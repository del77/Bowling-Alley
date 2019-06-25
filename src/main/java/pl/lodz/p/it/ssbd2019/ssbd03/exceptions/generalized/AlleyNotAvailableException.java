package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class AlleyNotAvailableException extends SsbdApplicationException {

    private static String code = "alleyNotAvailable";

    public AlleyNotAvailableException() {
        super();
    }

    public AlleyNotAvailableException(String message) {
        super(message);
    }

    public AlleyNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlleyNotAvailableException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }

}
