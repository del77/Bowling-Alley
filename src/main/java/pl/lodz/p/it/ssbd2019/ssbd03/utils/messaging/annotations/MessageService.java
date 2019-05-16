package pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja pomocnicza wykorzystywana przy wstrzykiwaniu obiektów dziedziczących po interfejsie Messenger.
 * @see pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger
 */
@Qualifier
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface MessageService {
    String value() default "NONE";
}
