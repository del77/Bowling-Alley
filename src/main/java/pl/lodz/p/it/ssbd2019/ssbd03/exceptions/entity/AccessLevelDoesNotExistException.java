package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class AccessLevelDoesNotExistException extends EntityRetrievalException {

    private static String code = "accessLevelDoesNotExist";

    public AccessLevelDoesNotExistException() { }

    public AccessLevelDoesNotExistException(String message) {
        super(message);
    }

    public AccessLevelDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessLevelDoesNotExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
