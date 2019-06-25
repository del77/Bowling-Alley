package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class ReservationItemOptimisticLockException extends DataAccessException {
    
    private static final String CODE = "reservationNotUpToDate";
    
    public ReservationItemOptimisticLockException() {
    }
    
    public ReservationItemOptimisticLockException(String message) {
        super(message);
    }
    
    public ReservationItemOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ReservationItemOptimisticLockException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getCode(){
        return CODE;
    }
}