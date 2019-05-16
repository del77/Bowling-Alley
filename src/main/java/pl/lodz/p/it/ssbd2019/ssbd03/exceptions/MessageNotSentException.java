package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class MessageNotSentException extends Exception {

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
