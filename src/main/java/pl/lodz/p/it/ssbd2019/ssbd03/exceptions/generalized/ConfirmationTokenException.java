package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class ConfirmationTokenException extends SsbdApplicationException {

    public ConfirmationTokenException() {
    }

    public ConfirmationTokenException(String message) {
        super(message);
    }

    public ConfirmationTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfirmationTokenException(Throwable cause) {
        super(cause);
    }
}
