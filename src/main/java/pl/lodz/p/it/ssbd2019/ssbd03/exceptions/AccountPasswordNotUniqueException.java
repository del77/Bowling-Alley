package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class AccountPasswordNotUniqueException extends Exception {
    public AccountPasswordNotUniqueException() {}

    public AccountPasswordNotUniqueException(String message) {
        super(message);
    }

    public AccountPasswordNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountPasswordNotUniqueException(Throwable cause) {
        super(cause);
    }
}
