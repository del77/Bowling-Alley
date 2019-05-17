package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ConfirmationToken;

public class ConfirmationTokenExpiredException extends ConfirmationTokenException {
    public ConfirmationTokenExpiredException() {
    }

    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }

    public ConfirmationTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfirmationTokenExpiredException(Throwable cause) {
        super(cause);
    }
}
