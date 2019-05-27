package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class EntityUpdateException extends DataAccessException {

    private static String code = "entityNotUpdated";

    public EntityUpdateException() {
    }

    public EntityUpdateException(String message) {
        super(message);
    }

    public EntityUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityUpdateException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
