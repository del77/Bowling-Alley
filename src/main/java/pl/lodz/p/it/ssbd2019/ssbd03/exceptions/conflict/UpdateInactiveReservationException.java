package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class UpdateInactiveReservationException extends SsbdApplicationException {
    private static final String code = "updateInactiveReservation";

    public UpdateInactiveReservationException() {
    }

    public UpdateInactiveReservationException(String message) {
        super(message);
    }

    public UpdateInactiveReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateInactiveReservationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode() {
        return code;
    }


}
