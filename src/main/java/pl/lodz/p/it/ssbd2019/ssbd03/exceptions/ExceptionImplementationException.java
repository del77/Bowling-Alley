package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class ExceptionImplementationException extends RuntimeException {
    public ExceptionImplementationException() { }
    
    public ExceptionImplementationException(String message) {
        super(message);
    }
    
    public ExceptionImplementationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ExceptionImplementationException(Throwable cause) {
        super(cause);
    }
}
