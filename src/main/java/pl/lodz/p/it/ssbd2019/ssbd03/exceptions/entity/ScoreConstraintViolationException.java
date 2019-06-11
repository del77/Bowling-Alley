package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class ScoreConstraintViolationException extends DataAccessException {
    private static String code = "scoreConstraintViolationException";

    public ScoreConstraintViolationException() {
        super();
    }

    public ScoreConstraintViolationException(String message) {
        super(message);
    }

    public ScoreConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScoreConstraintViolationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
