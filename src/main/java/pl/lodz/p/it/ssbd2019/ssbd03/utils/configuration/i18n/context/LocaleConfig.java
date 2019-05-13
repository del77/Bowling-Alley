package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context;

import java.util.Locale;

/**
 * Interfejs dla klas opowiadających za przechowywanie konfiguracji dla języka.
 */
public interface LocaleConfig {
    static final String PROPERTIES_CATALOG = "/WEB-INF/classes/";
    Locale locale();
    String filePath();
}
