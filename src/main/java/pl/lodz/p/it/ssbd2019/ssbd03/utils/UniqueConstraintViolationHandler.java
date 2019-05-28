package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.postgresql.util.PSQLException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.MissingRequiredConstructorException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.NotUniqueLoginException;

import java.lang.reflect.InvocationTargetException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UniqueConstraintViolationHandler {
    
    public static <T extends Throwable> void handleNotUniqueLoginOrEmailException(
            Throwable originalException,
            Class<T> exceptionType) throws NotUniqueLoginException, NotUniqueEmailException, T {
        Throwable t = originalException.getCause();
        while ((t != null) && !(t instanceof PSQLException)) {
            t = t.getCause();
            if (t.getMessage().contains("login")) {
                throw new NotUniqueLoginException();
            } else if (t.getMessage().contains("email")) {
                throw new NotUniqueEmailException();
            }
        }
        
        try {
            throw exceptionType.getConstructor(String.class, Throwable.class)
                    .newInstance(originalException.getMessage(), originalException);
        } catch (NoSuchMethodException | InvocationTargetException |
                IllegalArgumentException | IllegalAccessException | InstantiationException e) {
            throw new MissingRequiredConstructorException(String.format(
                    "Exception of type %s doesn't have a constructor taking throwable as a parameter",
                    exceptionType.getCanonicalName()
            ), e);
        }
    }
    
    public static <T extends Throwable> void handleNotUniqueEmailException(
            Throwable originalException,
            Class<T> exceptionType) throws NotUniqueEmailException, T {
        Throwable t = originalException.getCause();
        while ((t != null) && !(t instanceof PSQLException)) {
            t = t.getCause();
            if (t.getMessage().contains("email")) {
                throw new NotUniqueEmailException();
            }
        }
        
        try {
            throw exceptionType.getConstructor(String.class, Throwable.class)
                    .newInstance(originalException.getMessage(), originalException);
        } catch (NoSuchMethodException | InvocationTargetException |
                IllegalArgumentException | IllegalAccessException | InstantiationException e) {
            throw new MissingRequiredConstructorException(String.format(
                    "Exception of type %s doesn't have a constructor taking throwable as a parameter",
                    exceptionType.getCanonicalName()
            ), e);
        }
    }
}
