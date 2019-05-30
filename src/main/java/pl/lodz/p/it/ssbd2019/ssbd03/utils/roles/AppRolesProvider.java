package pl.lodz.p.it.ssbd2019.ssbd03.utils.roles;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.PropertyManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Klasa pomocnicza odpowiadająca za pobieranie nazw poziomów dostępu z pliku properties
 */

@ApplicationScoped
public class AppRolesProvider {
    @Inject
    private PropertyManager propertyManager;

    @Inject
    private ServletContext servletContext;

    private static final String ROLES_PROPERTIES = "/WEB-INF/classes/roles.properties";

    private Properties properties;

    public String getAdmin()  {
        if(properties == null) {
            try {
                properties = loadRoles();
            } catch (PropertiesLoadException e) {
                e.printStackTrace();
            }
        }
        return properties.getProperty("roles.admin");
    }

    public String getEmployee() {
        if(properties == null) {
            try {
                properties = loadRoles();
            } catch (PropertiesLoadException e) {
                e.printStackTrace();
            }
        }
        return properties.getProperty("roles.employee");
    }

    public String getClient() {
        if(properties == null) {
            try {
                properties = loadRoles();
            } catch (PropertiesLoadException e) {
                e.printStackTrace();
            }
        }
        return properties.getProperty("roles.client");
    }

    public String getUnconfirmed() {
        if(properties == null) {
            try {
                properties = loadRoles();
            } catch (PropertiesLoadException e) {
                e.printStackTrace();
            }
        }
        return properties.getProperty("roles.unconfirmed");
    }

    private Properties loadRoles() throws PropertiesLoadException {
        Path path = Paths.get(servletContext.getRealPath(ROLES_PROPERTIES));
        return propertyManager.loadProperties(path);
    }

}