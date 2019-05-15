package pl.lodz.p.it.ssbd2019.ssbd03.exceptions;

public class PropertiesLoadException extends Exception {
    public PropertiesLoadException() {
    }

    public PropertiesLoadException(String message) {
        super(message);
    }

    public PropertiesLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
