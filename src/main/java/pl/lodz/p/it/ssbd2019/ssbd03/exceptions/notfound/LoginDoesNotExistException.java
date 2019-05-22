package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound;

public class LoginDoesNotExistException extends NotFoundException {
    public LoginDoesNotExistException() { }
    
    public LoginDoesNotExistException(String message) {
        super(message);
    }
    
    public LoginDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LoginDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
