package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class CommentAddingException extends SsbdApplicationException {
    private static final String code = "cannotAddComment";

    public CommentAddingException() {
    }

    public CommentAddingException(String message) {
        super(message);
    }

    public CommentAddingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentAddingException(Throwable cause) {
        super(cause);
    }

    public CommentAddingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getCode() {
        return code;
    }
}
