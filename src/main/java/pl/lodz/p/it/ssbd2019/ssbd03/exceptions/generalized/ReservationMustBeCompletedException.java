package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class ReservationMustBeCompletedException extends SsbdApplicationException {
    private static String code = "reservationMustBeCompleted";

    public ReservationMustBeCompletedException() { }

    public ReservationMustBeCompletedException(String message) {
        super(message);
    }

    public ReservationMustBeCompletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationMustBeCompletedException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
