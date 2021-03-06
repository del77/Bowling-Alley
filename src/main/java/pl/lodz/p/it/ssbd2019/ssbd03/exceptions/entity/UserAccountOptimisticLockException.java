package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class UserAccountOptimisticLockException extends DataAccessException {

    private static String code = "userNotUpToDate";

    public UserAccountOptimisticLockException() {
    }

    public UserAccountOptimisticLockException(String message) {
        super(message);
    }

    public UserAccountOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountOptimisticLockException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
