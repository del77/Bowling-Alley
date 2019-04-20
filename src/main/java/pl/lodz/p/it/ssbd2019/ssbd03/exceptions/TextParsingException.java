package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class TextParsingException extends Exception{
    public TextParsingException() {
    }

    public TextParsingException(String message) {
        super(message);
    }

    public TextParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextParsingException(Throwable cause) {
        super(cause);
    }
}
