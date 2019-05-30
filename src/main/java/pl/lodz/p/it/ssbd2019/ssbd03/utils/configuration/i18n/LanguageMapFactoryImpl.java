package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.PropertyManager;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LocaleConfig;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Klasa przechowująca oraz wczytująca powiązania dla języka, tj. mapę mapowań między kluczem a wiadomością.
 */
@SessionScoped
public class LanguageMapFactoryImpl implements Serializable, LanguageMapFactory {

    @Inject
    private PropertyManager propertyManager;

    @Inject
    private ServletContext servletContext;

    private Hashtable<LocaleConfig, Hashtable<Object, Object>> loadedData = new Hashtable<>();
    private LocaleConfig lastLoaded;

    /**
     * Metoda zwracająca mapę powiązan dla zadanej konfiguracji.
     * W przypadku, gdy poprzednia konfiguracja jest tożsama, wczytuje już istniejącą mapę dla niej.
     *
     * @param localeConfig konfiguracja dla zadanego Locale.
     * @return Mapę powiązań kluczy z wiadomościami.
     * @throws PropertiesLoadException w przypadku, gdy wystapi błąd wczytywania pliku z właściwościami sprecyowanego
     *                                 w obiekcie localeConfig.
     */
    @Override
    public synchronized Hashtable<Object, Object> getLanguageMap(LocaleConfig localeConfig) throws PropertiesLoadException {
        if (this.loadedData.contains(localeConfig)) {
            this.lastLoaded = localeConfig;
            return this.loadedData.get(localeConfig);
        }
        String realPath = servletContext.getRealPath(localeConfig.filePath());
        if (realPath == null) {
            throw new PropertiesLoadException("Couldn't retrieve real path of element.");
        }
        Properties currentLanguageTable = propertyManager.loadProperties(Paths.get(realPath));
        this.loadedData.put(localeConfig, currentLanguageTable);
        this.lastLoaded = localeConfig;
        return currentLanguageTable;
    }

    @Override
    public LocaleConfig getLastLoadedLocaleConfig() {
        return this.lastLoaded;
    }
}