package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class ServiceRequestOptimisticLockException extends DataAccessException {

    private static String code = "serviceRequestNotUpToDate";

    public ServiceRequestOptimisticLockException() {
    }

    public ServiceRequestOptimisticLockException(String message) {
        super(message);
    }

    public ServiceRequestOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRequestOptimisticLockException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode(){
        return code;
    }
}
