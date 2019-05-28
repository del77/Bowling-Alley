package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class UserIdDoesNotExistException extends EntityRetrievalException {

    private static String code = "userIdDoesNotExist";

    public UserIdDoesNotExistException() { }

    public UserIdDoesNotExistException(String message) {
        super(message);
    }

    public UserIdDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIdDoesNotExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
