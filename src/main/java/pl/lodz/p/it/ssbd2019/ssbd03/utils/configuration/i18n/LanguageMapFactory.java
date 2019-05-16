package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LocaleConfig;

import java.util.Hashtable;

/**
 * Interfejs dla obiektów pozwalających wczytywać oraz przechowywać stan powiązań językowych.
 */
public interface LanguageMapFactory {
    Hashtable<Object, Object> getLanguageMap(LocaleConfig localeConfig) throws PropertiesLoadException;
    LocaleConfig getLastLoadedLocaleConfig();
}
