package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class ItemOptimisticLockException extends DataAccessException{
    private final static String code = "itemNotUpToDate";

    public ItemOptimisticLockException() {
    }

    public ItemOptimisticLockException(String message) {
        super(message);
    }

    public ItemOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemOptimisticLockException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
