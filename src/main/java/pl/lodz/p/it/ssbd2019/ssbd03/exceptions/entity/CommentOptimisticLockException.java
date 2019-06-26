package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class CommentOptimisticLockException extends DataAccessException {

    private static final String CODE = "commentNotUpToDate";

    public CommentOptimisticLockException() {
    }

    public CommentOptimisticLockException(String message) {
        super(message);
    }

    public CommentOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentOptimisticLockException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode() {
        return CODE;
    }
}
