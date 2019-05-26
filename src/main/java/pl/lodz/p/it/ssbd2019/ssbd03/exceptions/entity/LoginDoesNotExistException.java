package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class LoginDoesNotExistException extends EntityRetrievalException {

    private static String code = "loginDoesNotExist";

    public LoginDoesNotExistException() { }
    
    public LoginDoesNotExistException(String message) {
        super(message);
    }
    
    public LoginDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LoginDoesNotExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
