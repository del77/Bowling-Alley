package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.StateConflictedException;

public class ValidationException extends StateConflictedException {

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
