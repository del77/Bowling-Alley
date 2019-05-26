package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class NotUniqueEmailException extends DataAccessException {

    private static String code = "emailNotUnique";

    public NotUniqueEmailException() {
        super();
    }

    public NotUniqueEmailException(String message) {
        super(message);
    }

    public NotUniqueEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniqueEmailException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }

}
