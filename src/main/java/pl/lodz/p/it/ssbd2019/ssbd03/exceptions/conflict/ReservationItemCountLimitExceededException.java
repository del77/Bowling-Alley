package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class ReservationItemCountLimitExceededException extends SsbdApplicationException {
    
    private static final String CODE = "countLimitExceeded";
    
    public ReservationItemCountLimitExceededException() {
    }
    
    public ReservationItemCountLimitExceededException(String message) {
        super(message);
    }
    
    public ReservationItemCountLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ReservationItemCountLimitExceededException(Throwable cause) {
        super(cause);
    }
    
    public ReservationItemCountLimitExceededException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    @Override
    public String getCode(){
        return CODE;
    }
}
