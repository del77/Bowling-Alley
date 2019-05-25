package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class StateConflictedException extends SsbdApplicationException {

    public StateConflictedException() {
    }

    public StateConflictedException(String message) {
        super(message);
    }

    public StateConflictedException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateConflictedException(Throwable cause) {
        super(cause);
    }

    public StateConflictedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
