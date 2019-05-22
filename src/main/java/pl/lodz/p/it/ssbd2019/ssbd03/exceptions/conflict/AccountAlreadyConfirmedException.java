package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

public class AccountAlreadyConfirmedException extends StateConflictedException {

    public AccountAlreadyConfirmedException() {
    }

    public AccountAlreadyConfirmedException(String message) {
        super(message);
    }

    public AccountAlreadyConfirmedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountAlreadyConfirmedException(Throwable cause) {
        super(cause);
    }
}
