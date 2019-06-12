package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class ReservationDoesNotExistException extends EntityRetrievalException {

    private static String code = "reservationDoesNotExist";

    public ReservationDoesNotExistException() { }

    public ReservationDoesNotExistException(String message) {
        super(message);
    }

    public ReservationDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationDoesNotExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
