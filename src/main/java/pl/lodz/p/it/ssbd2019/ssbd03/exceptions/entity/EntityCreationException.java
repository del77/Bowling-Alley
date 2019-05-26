package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class EntityCreationException extends DataAccessException {

    private static String code = "cannotCreateEntity";

    public EntityCreationException() {
    }

    public EntityCreationException(String message) {
        super(message);
    }

    public EntityCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityCreationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
