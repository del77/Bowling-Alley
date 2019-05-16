package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja wykorzystywana do wstrzykiwania CDI obiektów reprezentujących konkretne konfiguracje LocaleConfig
 * @see pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LocaleConfig
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface KnownLocale {
    String value() default "NONE";
}
