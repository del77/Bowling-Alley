package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class DataParseException extends SsbdApplicationException {

    private static String code = "cannotParseData";

    public DataParseException() {
    }

    public DataParseException(String message) {
        super(message);
    }

    public DataParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataParseException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode() {
        return code;
    }
}
