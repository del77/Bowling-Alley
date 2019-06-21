package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

public class ReservationAlreadyInactiveException extends StateConflictedException {
    private static String code = "reservationAlreadyInactive";

    public ReservationAlreadyInactiveException() {
    }

    public ReservationAlreadyInactiveException(String message) {
        super(message);
    }

    public ReservationAlreadyInactiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationAlreadyInactiveException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
