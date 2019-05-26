package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class EntityRetrievalException extends DataAccessException {

    private static String code = "cannotRetrieveEntity";

    public EntityRetrievalException() {
    }

    public EntityRetrievalException(String message) {
        super(message);
    }

    public EntityRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityRetrievalException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
