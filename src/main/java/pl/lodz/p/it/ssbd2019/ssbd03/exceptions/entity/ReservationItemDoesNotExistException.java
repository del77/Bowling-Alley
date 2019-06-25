package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class ReservationItemDoesNotExistException extends EntityRetrievalException {
    
    private static final String CODE = "reservationItemDoesNotExist";
    
    public ReservationItemDoesNotExistException() { }
    
    public ReservationItemDoesNotExistException(String message) {
        super(message);
    }
    
    public ReservationItemDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ReservationItemDoesNotExistException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getCode(){
        return CODE;
    }
}
