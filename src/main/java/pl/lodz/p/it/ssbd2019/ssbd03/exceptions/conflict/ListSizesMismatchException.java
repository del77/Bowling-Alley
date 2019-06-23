package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class ListSizesMismatchException extends SsbdApplicationException {
    
    private static final String CODE = "listSizesMismatched";
    
    public ListSizesMismatchException() { }
    
    public ListSizesMismatchException(String message) {
        super(message);
    }
    
    public ListSizesMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ListSizesMismatchException(Throwable cause) {
        super(cause);
    }
    
    public String getCode(){
        return CODE;
    }
}
