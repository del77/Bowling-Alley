package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(inherited = true, rollback = true)
public class SsbdApplicationException extends Exception{

    public SsbdApplicationException() {
    }

    public SsbdApplicationException(String message) {
        super(message);
    }

    public SsbdApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SsbdApplicationException(Throwable cause) {
        super(cause);
    }

    public SsbdApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
