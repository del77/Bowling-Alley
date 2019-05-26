package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class PropertiesLoadException extends SsbdApplicationException {

    private static String code = "cannotLoadProperties";

    public PropertiesLoadException() {
    }

    public PropertiesLoadException(String message) {
        super(message);
    }

    public PropertiesLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
