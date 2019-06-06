package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class PhoneLengthConstraintViolationException extends DataAccessException {
    private static String code = "phoneLengthConstraintViolation";

    public PhoneLengthConstraintViolationException() {
        super();
    }

    public PhoneLengthConstraintViolationException(String message) {
        super(message);
    }

    public PhoneLengthConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneLengthConstraintViolationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
