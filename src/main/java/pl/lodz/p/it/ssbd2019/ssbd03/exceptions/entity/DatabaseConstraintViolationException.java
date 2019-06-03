package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class DatabaseConstraintViolationException extends DataAccessException{
    private static String code = "databaseConstraintViolation";

    public DatabaseConstraintViolationException() {
        super();
    }

    public DatabaseConstraintViolationException(String message) {
        super(message);
    }

    public DatabaseConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseConstraintViolationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }

}
