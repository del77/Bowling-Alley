package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

public class TokenExpiredException extends StateConflictedException {
    public TokenExpiredException() { }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpiredException(Throwable cause) {
        super(cause);
    }
}
