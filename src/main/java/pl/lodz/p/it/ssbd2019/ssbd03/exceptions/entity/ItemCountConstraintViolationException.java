package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class ItemCountConstraintViolationException extends DataAccessException {
    private final static String code = "itemCountConstraintViolation";

    public ItemCountConstraintViolationException() {
        super();
    }

    public ItemCountConstraintViolationException(String message) {
        super(message);
    }

    public ItemCountConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemCountConstraintViolationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
