package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.CommentAddingException;

public class CommentNotOwnedReservationException extends CommentAddingException {

    private static final String CODE = "commentNotOwnedReservation";

    public CommentNotOwnedReservationException() {
    }

    public CommentNotOwnedReservationException(String message) {
        super(message);
    }

    public CommentNotOwnedReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentNotOwnedReservationException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return CODE;
    }
}
