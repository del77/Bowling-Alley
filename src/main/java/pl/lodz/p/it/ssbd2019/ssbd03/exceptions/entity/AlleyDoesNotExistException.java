package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;

public class AlleyDoesNotExistException extends NotFoundException {

    private static String code = "alleyDoesNotExist";

    public AlleyDoesNotExistException() { }

    public AlleyDoesNotExistException(String message) {
        super(message);
    }

    public AlleyDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlleyDoesNotExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
    
}
