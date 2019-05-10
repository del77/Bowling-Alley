package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;

@SessionScoped
public class I18nManager implements Serializable {
    private static final String NONE = "none";
    private static final String ENGLISH = "english";
    private static final String POLISH = "polish";
    private static final String POLISH_PROPERTIES_FILE = "locale.properties";
    private static final String ENGLISH_PROPERTIES_FILE = "locale_en.properties";
    private static final String PROPERTIES_CATALOG = "/WEB-INF/classes/";


    @Inject
    PropertyManager propertyManager;

    @Inject
    private ServletContext servletContext;

    private Map<String, String> knownLanguages = new HashMap<String, String>() {
        {
            put(POLISH, POLISH_PROPERTIES_FILE);
            put(ENGLISH, ENGLISH_PROPERTIES_FILE);
        }
    };

    private Hashtable<Object, Object> currentLanguageTable = null;
    private String selectedLanguage = POLISH;

    public Hashtable<Object, Object> changeLanguage(String langName) throws PropertiesLoadException {
        langName = langName.toLowerCase();
        String languagePropertiesFile = knownLanguages.getOrDefault(langName, NONE);
        if (languagePropertiesFile.equalsIgnoreCase(NONE)) {
            throw new IllegalArgumentException("No properties file allowed for language " + langName);
        }
        String realPath = servletContext.getRealPath(PROPERTIES_CATALOG);
        this.currentLanguageTable = propertyManager.loadProperties(Paths.get(realPath, languagePropertiesFile));
        this.selectedLanguage = langName;
        return currentLanguageTable;
    }

    public Hashtable<Object, Object> getLanguageMap() throws PropertiesLoadException {
        if (currentLanguageTable == null) {
            if (selectedLanguage != null) {
                return changeLanguage(selectedLanguage);
            }
            return changeLanguage(POLISH);
        }
        return currentLanguageTable;
    }
}