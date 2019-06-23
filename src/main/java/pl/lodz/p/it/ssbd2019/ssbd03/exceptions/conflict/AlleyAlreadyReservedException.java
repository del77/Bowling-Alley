package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class AlleyAlreadyReservedException extends SsbdApplicationException {
    
    private static final String CODE = "alleyAlreadyTaken";
    
    public AlleyAlreadyReservedException() { }
    
    public AlleyAlreadyReservedException(String message) {
        super(message);
    }
    
    public AlleyAlreadyReservedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AlleyAlreadyReservedException(Throwable cause) {
        super(cause);
    }
    
    public String getCode(){
        return CODE;
    }
}