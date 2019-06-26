package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public class SsbdTransactionRolledbackException extends SsbdApplicationException {
    
    private static final String CODE = "transactionRolledback";
    
    public SsbdTransactionRolledbackException() {
    }
    
    public SsbdTransactionRolledbackException(String message) {
        super(message);
    }
    
    public SsbdTransactionRolledbackException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SsbdTransactionRolledbackException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getCode(){
        return CODE;
    }
}