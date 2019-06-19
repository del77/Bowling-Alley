package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class AlleyDoesNotExistException extends EntityRetrievalException {

    private static String code = "alleyDoesNotExist";

    public AlleyDoesNotExistException() { }

    public AlleyDoesNotExistException(String message) {
        super(message);
    }

    public AlleyDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlleyDoesNotExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
    
}
