package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class UpdateExpiredReservationExcetpion extends SsbdApplicationException {
    private static final String code = "updateExpiredReservation";

    public UpdateExpiredReservationExcetpion() {
    }

    public UpdateExpiredReservationExcetpion(String message) {
        super(message);
    }

    public UpdateExpiredReservationExcetpion(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateExpiredReservationExcetpion(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode() {
        return code;
    }
}
