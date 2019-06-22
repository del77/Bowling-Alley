package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class DatesNotInPresentOrFuture  extends DataAccessException {
    private static final String CODE = "datesNotFutureViolation";
    
    public DatesNotInPresentOrFuture() {
        super();
    }
    
    public DatesNotInPresentOrFuture(String message) {
        super(message);
    }
    
    public DatesNotInPresentOrFuture(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DatesNotInPresentOrFuture(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getCode(){
        return CODE;
    }
}