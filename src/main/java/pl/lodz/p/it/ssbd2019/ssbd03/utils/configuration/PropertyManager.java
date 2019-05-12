package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@ApplicationScoped
public class PropertyManager {
    private ClassLoader classLoader = Thread
            .currentThread()
            .getContextClassLoader();

    public Properties loadProperties(Path filePath) throws PropertiesLoadException {
        if (!Files.exists(filePath)) {
            throw new PropertiesLoadException(filePath + " doesn't exist.");
        }
        Properties properties = new Properties();
        try {
            File file = new File(filePath.toString());
            try (
                    FileInputStream fileInputStream = new FileInputStream(file);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                properties.load(bufferedReader);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            throw new PropertiesLoadException("Error during reading of " + filePath + ", " + e.getLocalizedMessage());
        }
        return properties;
    }
}
