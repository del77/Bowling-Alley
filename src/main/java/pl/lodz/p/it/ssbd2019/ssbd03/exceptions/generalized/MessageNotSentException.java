package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class MessageNotSentException extends SsbdApplicationException {


    public MessageNotSentException() {
        super();
    }

    public MessageNotSentException(String message) {
        super(message);
    }

    public MessageNotSentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageNotSentException(Throwable cause) {
        super(cause);
    }

}
