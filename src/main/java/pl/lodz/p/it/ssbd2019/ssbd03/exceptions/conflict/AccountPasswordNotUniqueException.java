package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

public class AccountPasswordNotUniqueException extends StateConflictedException {
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
