package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;

import java.nio.file.Path;
import java.util.Properties;

public interface PropertyManager {
    Properties loadProperties(Path filePath) throws PropertiesLoadException;
}
