package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound;

public class TokenNotFoundException extends NotFoundException {

    public TokenNotFoundException() {
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenNotFoundException(Throwable cause) {
        super(cause);
    }
}
