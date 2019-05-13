package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.PropertyManager;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

@SessionScoped
public class I18nManager implements Serializable {
    public static final Locale POLISH_LOCALE = new Locale("pl", "PL");
    public static final Locale ENGLISH_LOCALE = Locale.ENGLISH;

    private static final String POLISH_PROPERTIES_FILE = "locale.properties";
    private static final String ENGLISH_PROPERTIES_FILE = "locale_en.properties";
    private static final String PROPERTIES_CATALOG = "/WEB-INF/classes/";

    @Inject
    private PropertyManager propertyManager;

    @Inject
    private ServletContext servletContext;

    private final Map<Locale, String> knownLanguages = new Hashtable<Locale, String>() {
        {
            put(POLISH_LOCALE, POLISH_PROPERTIES_FILE);
            put(ENGLISH_LOCALE, ENGLISH_PROPERTIES_FILE);
        }
    };

    private Hashtable<Object, Object> currentLanguageTable = null;
    private Locale selectedLanguage = POLISH_LOCALE;

    public Hashtable<Object, Object> changeLanguage(Locale locale) throws PropertiesLoadException {
        if (knownLanguages.containsKey(locale)) {
            String filePath = knownLanguages.get(locale);
            String realPath = servletContext.getRealPath(PROPERTIES_CATALOG);
            this.currentLanguageTable = propertyManager.loadProperties(Paths.get(realPath, filePath));
            this.selectedLanguage = locale;
            return currentLanguageTable;
        } else {
            throw new PropertiesLoadException("Unknown locale for the site.");
        }
    }

    public Locale getCurrentLocale() {
        return selectedLanguage;
    }

    public Hashtable<Object, Object> getLanguageMap() throws PropertiesLoadException {
        if (currentLanguageTable == null) {
            if (selectedLanguage != null) {
                return changeLanguage(selectedLanguage);
            }
            return changeLanguage(POLISH_LOCALE);
        }
        return currentLanguageTable;
    }
}