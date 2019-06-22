package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound;

public class AlleyNotFoundException extends NotFoundException {
    
    private final static String code = "alleyNotFound";
    
    public AlleyNotFoundException() {
    }
    
    public AlleyNotFoundException(String message) {
        super(message);
    }
    
    public AlleyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AlleyNotFoundException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getCode(){
        return code;
    }
}
