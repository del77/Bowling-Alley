package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class TextParsingException extends SsbdApplicationException {

    private static String code = "textCannotBeParsed";

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

    @Override
    public String getCode(){
        return code;
    }
}
