package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.PropertyManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Klasa pomocnicza pobierająca klucze Recaptcha z pliku properties.
 */
@ApplicationScoped
public class RecaptchaKeysProvider {
    @Inject
    private PropertyManager propertyManager;

    @Inject
    private ServletContext servletContext;

    private RecaptchaKeys keys;
    private static final String RECAPTCHA_PROPERTIES = "/WEB-INF/classes/recaptcha.properties";

    public String getSecretKey() throws PropertiesLoadException {
        if(keys == null) {
            getRecaptchaProperties();
        }
        return keys.secretKey;
    }

    public String getSiteKey() throws PropertiesLoadException {
        if(keys == null) {
            getRecaptchaProperties();
        }
        return keys.siteKey;
    }

    private void getRecaptchaProperties() throws PropertiesLoadException {
        Path path = Paths.get(servletContext.getRealPath(RECAPTCHA_PROPERTIES));
        Properties properties = propertyManager.loadProperties(path);

        keys = new RecaptchaKeys();
        keys.siteKey = properties.getProperty("siteKey");
        keys.secretKey = properties.getProperty("secretKey");
    }

    private class RecaptchaKeys {
        private String siteKey;
        private String secretKey;
    }
}
