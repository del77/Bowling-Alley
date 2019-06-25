package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class DatesConstraintViolationException extends DataAccessException {
    private static final String CODE = "datesConstraintViolation";
    
    public DatesConstraintViolationException() {
        super();
    }
    
    public DatesConstraintViolationException(String message) {
        super(message);
    }
    
    public DatesConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DatesConstraintViolationException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getCode(){
        return CODE;
    }
}