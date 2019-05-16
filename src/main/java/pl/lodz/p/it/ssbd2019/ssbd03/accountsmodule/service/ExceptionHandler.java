package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.postgresql.util.PSQLException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ExceptionImplementationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueLoginException;

import java.lang.reflect.InvocationTargetException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ExceptionHandler {
    
    static <T extends Throwable> void handleNotUniqueLoginOrEmailException(
            Throwable originalException,
            Class<T> exceptionType) throws NotUniqueLoginException, NotUniqueEmailException, T {
        Throwable t = originalException.getCause();
        while ((t != null) && !(t instanceof PSQLException)) {
            t = t.getCause();
            if (t.getMessage().contains("login")){
                throw new NotUniqueLoginException();
            } else if (t.getMessage().contains("email")) {
                throw new NotUniqueEmailException();
            }
        }
        
        try {
            throw exceptionType.getConstructor(String.class).newInstance(originalException.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException |
                IllegalArgumentException | IllegalAccessException | InstantiationException e) {
            throw new ExceptionImplementationException(String.format(
                    "Exception of type %s doesn't have a constructor taking throwable as a parameter",
                    exceptionType.getCanonicalName()
            ), e);
        }
    }
}
