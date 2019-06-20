package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class ReservationOptimisticLockException extends DataAccessException {

    private static String code = "reservationNotUpToDate";

    public ReservationOptimisticLockException() {
    }

    public ReservationOptimisticLockException(String message) {
        super(message);
    }

    public ReservationOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationOptimisticLockException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
