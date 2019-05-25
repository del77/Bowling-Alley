package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

public class MissingRequiredConstructorException extends RuntimeException {

    public MissingRequiredConstructorException() { }
    
    public MissingRequiredConstructorException(String message) {
        super(message);
    }
    
    public MissingRequiredConstructorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MissingRequiredConstructorException(Throwable cause) {
        super(cause);
    }
}
