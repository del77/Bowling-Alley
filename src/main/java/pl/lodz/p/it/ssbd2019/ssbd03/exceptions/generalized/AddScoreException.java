package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class AddScoreException extends SsbdApplicationException {

    private static String code = "addScoreException";

    public AddScoreException() {
    }

    public AddScoreException(String message) {
        super(message);
    }

    public AddScoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddScoreException(Throwable cause) {
        super(cause);
    }

    public AddScoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getCode(){
        return code;
    }
}
