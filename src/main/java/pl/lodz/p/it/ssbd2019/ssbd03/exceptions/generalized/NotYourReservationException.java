package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class NotYourReservationException extends SsbdApplicationException {
    
    private static final String CODE = "notYourReservation";
    
    public NotYourReservationException() {
        super();
    }
    
    public NotYourReservationException(String message) {
        super(message);
    }
    
    public NotYourReservationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NotYourReservationException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getCode(){
        return CODE;
    }
}
