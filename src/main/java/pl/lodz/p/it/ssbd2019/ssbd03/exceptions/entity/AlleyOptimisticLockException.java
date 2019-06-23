package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class AlleyOptimisticLockException extends DataAccessException{
    private static String code = "alleyNotUpToDate";

    public AlleyOptimisticLockException() { }

    public AlleyOptimisticLockException(String message) {
        super(message);
    }

    public AlleyOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlleyOptimisticLockException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
