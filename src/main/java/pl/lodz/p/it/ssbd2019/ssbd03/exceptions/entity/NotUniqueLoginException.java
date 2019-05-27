package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class NotUniqueLoginException extends DataAccessException {

    private static String code = "loginNotUnique";

    public NotUniqueLoginException() {
        super();
    }

    public NotUniqueLoginException(String message) {
        super(message);
    }

    public NotUniqueLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniqueLoginException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
