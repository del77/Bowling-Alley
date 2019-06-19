package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class NotUniqueAlleyNumberException extends DataAccessException {

    private static String code = "alleyNumberNotUnique";

    public NotUniqueAlleyNumberException() {
        super();
    }

    public NotUniqueAlleyNumberException(String message) {
        super(message);
    }

    public NotUniqueAlleyNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniqueAlleyNumberException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
